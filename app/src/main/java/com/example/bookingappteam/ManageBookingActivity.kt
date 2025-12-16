package com.example.bookingappteam

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookingappteam.databinding.ActivityManageBookingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.android.gms.tasks.Tasks
import java.text.SimpleDateFormat
import java.util.Locale

class ManageBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageBookingBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var bookingAdapter: BookingAdapter
    private val bookings = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        setupRecyclerView()
        loadAllBookings()

        binding.buttonCancelBooking.setOnClickListener { performCancellation() }
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter(bookings)
        binding.rvAllBookings.apply {
            layoutManager = LinearLayoutManager(this@ManageBookingActivity)
            adapter = bookingAdapter
        }
    }

    private fun loadAllBookings() {
        val scheduleRef = database.getReference("schedule")
        scheduleRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookings.clear()

                if (snapshot.exists()) {
                    for (dateSnapshot in snapshot.children) {
                        val rawDate = dateSnapshot.key ?: continue

                        for (roomSnapshot in dateSnapshot.children) {
                            val roomKey = roomSnapshot.key ?: continue

                            for (timeSnapshot in roomSnapshot.children) {
                                val bookingDetails = timeSnapshot.value as? HashMap<*, *> ?: continue
                                val rawTime = timeSnapshot.key ?: continue

                                val purpose = bookingDetails["purpose"]?.toString() ?: "N/A"
                                val numberOfStudents = bookingDetails["numberOfStudents"]?.toString() ?: "N/A"
                                val username = bookingDetails["username"]?.toString() ?: "N/A"

                                database.getReference("rooms").child(roomKey).child("name").get()
                                    .addOnSuccessListener { roomNameSnapshot ->
                                        val roomName = roomNameSnapshot.value as? String ?: roomKey

                                        val displayDate = try {
                                            val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(rawDate)
                                            SimpleDateFormat("MMM dd, yyyy", Locale.US).format(parsedDate!!)
                                        } catch (e: Exception) {
                                            rawDate
                                        }

                                        val displayTime = try {
                                            val parts = rawTime.split("-")
                                            val formatter = SimpleDateFormat("hh:mm a", Locale.US)
                                            val start = SimpleDateFormat("HHmm", Locale.US).parse(parts[0])
                                            val end = SimpleDateFormat("HHmm", Locale.US).parse(parts[1])
                                            "${formatter.format(start!!)} - ${formatter.format(end!!)}"
                                        } catch (e: Exception) {
                                            rawTime
                                        }

                                        val dbRef = timeSnapshot.ref
                                        bookings.add(
                                            Booking(roomName, displayDate, displayTime, purpose, numberOfStudents, username, dbRef)
                                        )
                                        bookingAdapter.notifyDataSetChanged()

                                        // Show/hide cancel button depending on list content
                                        if (bookings.isNotEmpty()) {
                                            binding.buttonCancelBooking.visibility = View.VISIBLE
                                            binding.tvNoReservations.visibility = View.GONE
                                        } else {
                                            binding.buttonCancelBooking.visibility = View.GONE
                                            binding.tvNoReservations.visibility = View.VISIBLE
                                        }
                                    }
                            }
                        }
                    }
                } else {
                    binding.buttonCancelBooking.visibility = View.GONE
                    binding.tvNoReservations.visibility = View.VISIBLE
                    bookingAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageBookingActivity, "Failed to load bookings.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun performCancellation() {
        val selectedBookings = bookingAdapter.getSelectedBookings()

        if (selectedBookings.isNotEmpty()) {
            val cancellationTasks = selectedBookings.mapNotNull { it.dbRef?.removeValue() }
            Tasks.whenAll(cancellationTasks)
                .addOnSuccessListener {
                    Toast.makeText(this, "Cancellation Successful!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Cancellation failed.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select a booking to cancel.", Toast.LENGTH_SHORT).show()
        }
    }
}
