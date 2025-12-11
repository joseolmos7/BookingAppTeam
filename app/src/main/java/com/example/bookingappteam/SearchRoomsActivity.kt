package com.example.bookingappteam

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappteam.databinding.ActivitySearchRoomsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SearchRoomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchRoomsBinding
    private lateinit var database: DatabaseReference
    private var username: String? = null
    private val selectedDate = Calendar.getInstance()
    private var timeValuesForSpinner: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        username = intent.getStringExtra("USERNAME")

        if (username == null) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        setupStudentSpinner()
        setupDateTimePickers()
        setupButtons()
    }

    private fun setupStudentSpinner() {
        val studentNumbers = (1..5).toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, studentNumbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.studentsSpinner.adapter = adapter
    }


    private fun setupDateTimePickers() {
        updateDateButtonText()
        updateHourSpinner()

        binding.btnSelectDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, month, dayOfMonth)
                    if (newDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        Toast.makeText(this, "Sundays are not available for booking", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        updateDateButtonText()
                        updateHourSpinner()
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }

    private fun updateDateButtonText() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        binding.btnSelectDate.text = sdf.format(selectedDate.time)
    }

    private fun updateHourSpinner() {
        val dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)
        val hourRange = when (dayOfWeek) {
            Calendar.SATURDAY -> (10..15)
            else -> (8..20)
        }

        val displayHours = mutableListOf<String>()
        val databaseHours = mutableListOf<String>()

        val dbFormat = SimpleDateFormat("HHmm", Locale.US)
        val displayFormat = SimpleDateFormat("hh:mm a", Locale.US)

        val now = Calendar.getInstance()
        val isToday = selectedDate.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                selectedDate.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
        val currentHour = now.get(Calendar.HOUR_OF_DAY)

        for (hour in hourRange) {
            if (isToday && hour <= currentHour) {
                continue // Skip past hours on the current day
            }

            val dbHourString = String.format("%02d00", hour)
            databaseHours.add(dbHourString)
            val date = dbFormat.parse(dbHourString)
            displayHours.add(displayFormat.format(date!!))
        }

        timeValuesForSpinner = databaseHours // Save for later use

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, displayHours)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerStartTime.adapter = adapter
        binding.spinnerEndTime.adapter = adapter
    }

    private fun setupButtons() {
        binding.btnSearch.setOnClickListener { searchAvailableRooms() }
        binding.btnReserve.setOnClickListener { reserveRoom() }
    }

    private fun searchAvailableRooms() {
        clearResults()

        val date = binding.btnSelectDate.text.toString()

        if (timeValuesForSpinner.isEmpty()
            || binding.spinnerStartTime.selectedItemPosition < 0
            || binding.spinnerEndTime.selectedItemPosition < 0
        ) {
            Toast.makeText(this, "No available time slots for this day.", Toast.LENGTH_SHORT).show()
            return
        }

        val startIndex = binding.spinnerStartTime.selectedItemPosition
        val endIndex = binding.spinnerEndTime.selectedItemPosition

        if (endIndex <= startIndex) {
            Toast.makeText(this, "End time must be after start time.", Toast.LENGTH_SHORT).show()
            return
        }

        // number of students from spinner
        val numberOfStudents = binding.studentsSpinner.selectedItem.toString().toInt()

        val roomsRef = database.child("rooms")
        val dayScheduleRef = database.child("schedule").child(date)

        roomsRef.get().addOnSuccessListener { allRoomsSnapshot ->
            val eligibleRooms = mutableSetOf<String>()

            for (roomSnap in allRoomsSnapshot.children) {
                val roomKey = roomSnap.key ?: continue

                // if capacity doesn't exist, assume very large (room is OK)
                val capacityLong = roomSnap.child("capacity").getValue(Long::class.java)
                val capacity = capacityLong?.toInt() ?: Int.MAX_VALUE

                if (capacity >= numberOfStudents) {
                    eligibleRooms.add(roomKey)
                }
            }

            dayScheduleRef.get().addOnSuccessListener { daySnapshot ->
                // rooms that have ANY booking on this date
                val bookedRooms = daySnapshot.children.mapNotNull { it.key }.toSet()

                val availableRooms = eligibleRooms - bookedRooms

                if (availableRooms.isEmpty()) {
                    binding.tvNoRooms.visibility = View.VISIBLE
                } else {
                    displayAvailableRooms(availableRooms)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load bookings for this date.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load rooms list.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayAvailableRooms(availableRooms: Set<String>) {
        for (roomKey in availableRooms) {
            database.child("rooms").child(roomKey).child("name").get().addOnSuccessListener {
                val radioButton = RadioButton(this)
                radioButton.text = it.value as? String
                radioButton.tag = roomKey
                binding.radioGroupRooms.addView(radioButton)
            }
        }
        binding.btnReserve.visibility = View.VISIBLE
    }

    private fun reserveRoom() {

        val startIndex = binding.spinnerStartTime.selectedItemPosition
        val endIndex = binding.spinnerEndTime.selectedItemPosition

        if (timeValuesForSpinner.isEmpty()
            || startIndex < 0
            || endIndex < 0
        ) {
            Toast.makeText(this, "Cannot reserve, selected time is not valid.", Toast.LENGTH_SHORT).show()
            return
        }

        if (endIndex <= startIndex) {
            Toast.makeText(this, "End time must be after start time.", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ NEW: use start + end
        val startTime = timeValuesForSpinner[startIndex]   // e.g. "0800"
        val endTime = timeValuesForSpinner[endIndex]       // e.g. "1000"
        val timeRangeKey = "$startTime-$endTime"          // "0800-1000"

        val selectedRadioButtonId = binding.radioGroupRooms.checkedRadioButtonId
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select a room to reserve.", Toast.LENGTH_SHORT).show()
            return
        }

        val purpose = binding.purposeEditText.text.toString().trim()
        if (purpose.isEmpty()) {
            Toast.makeText(this, "Please enter the purpose of booking.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val roomKey = selectedRadioButton.tag as String
        val date = binding.btnSelectDate.text.toString()
        val numberOfStudents = binding.studentsSpinner.selectedItem.toString()

        val bookingDetails = hashMapOf(
            "username" to username,
            "purpose" to purpose,
            "numberOfStudents" to numberOfStudents,
            // optional but useful:
            "startTime" to startTime,
            "endTime" to endTime
        )

        // ✅ NEW STRUCTURE: schedule/date/room/start-end
        database.child("schedule")
            .child(date)
            .child(roomKey)
            .child(timeRangeKey)
            .setValue(bookingDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("USERNAME", username)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearResults() {
        binding.radioGroupRooms.removeAllViews()
        binding.btnReserve.visibility = View.GONE
        binding.tvNoRooms.visibility = View.GONE
    }


}