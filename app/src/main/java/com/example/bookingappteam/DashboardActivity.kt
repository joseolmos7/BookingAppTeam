package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappteam.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("USERNAME")

        binding.cardMyBookings.setOnClickListener {
            val intent = Intent(this, MyBookingsActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        binding.cardSearchRooms.setOnClickListener {
            val intent = Intent(this, SearchRoomsActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }
}