package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookingappteam.databinding.ActivityMyBookingsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyBookingsBinding
    private lateinit var database: FirebaseDatabase
    private var username: String? = null
    private lateinit var bookingAdapter: BookingAdapter
    private val bookings = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        username = intent.getStringExtra("USERNAME")

        if (username == null) {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setupRecyclerView()
        loadUserBookings()

        binding.btnCancel.setOnClickListener { performCancellation() }
    }

    private fun setupRecyclerView() {
        bookingAdapter = BookingAdapter(bookings)
        binding.rvBookings.apply {
            layoutManager = LinearLayoutManager(this@MyBookingsActivity)
            adapter = bookingAdapter
        }
    }

    private fun loadUserBookings() {
        val scheduleRef = database.getReference("schedule")
        scheduleRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookings.clear()
                var bookingsFound = false
                for (dateSnapshot in snapshot.children) {
                    for (timeSnapshot in dateSnapshot.children) {
                        for (roomSnapshot in timeSnapshot.children) {
                            val bookingDetails = roomSnapshot.value as? HashMap<*, *>
                            if (bookingDetails != null && bookingDetails["username"] == username) {
                                bookingsFound = true
                                val date = dateSnapshot.key!!
                                val time = timeSnapshot.key!!
                                val roomKey = roomSnapshot.key!!
                                val purpose = bookingDetails["purpose"] as? String ?: "N/A"
                                val numberOfStudents = bookingDetails["numberOfStudents"] as? String ?: "N/A"

                                database.getReference("rooms").child(roomKey).child("name").get().addOnSuccessListener { roomNameSnapshot ->
                                    val roomName = roomNameSnapshot.value as? String ?: roomKey
                                    val displayDate = SimpleDateFormat("MMM dd, yyyy", Locale.US).format(SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)!!)
                                    val displayTime = SimpleDateFormat("hh:mm a", Locale.US).format(SimpleDateFormat("HHmm", Locale.US).parse(time)!!)
                                    val dbRef = roomSnapshot.ref
                                    bookings.add(Booking(roomName, displayDate, displayTime, purpose, numberOfStudents, username!!, dbRef))
                                    bookingAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }
                if (bookingsFound) {
                    binding.btnCancel.visibility = View.VISIBLE
                    binding.tvNoReservations.visibility = View.GONE
                } else {
                    binding.btnCancel.visibility = View.GONE
                    binding.tvNoReservations.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyBookingsActivity, "Failed to load bookings.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performCancellation() {
        val selectedBookings = bookingAdapter.getSelectedBookings()

        if (selectedBookings.isNotEmpty()) {
            selectedBookings.forEach { booking ->
                booking.dbRef?.removeValue()
            }
            Toast.makeText(this, "Cancellation Successful!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("USERNAME", username)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please select a booking to cancel.", Toast.LENGTH_SHORT).show()
        }
    }
}