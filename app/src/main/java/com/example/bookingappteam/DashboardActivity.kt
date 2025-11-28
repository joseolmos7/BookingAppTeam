package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappteam.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardAvailableRooms.setOnClickListener {
            val intent = Intent(this, AvailableRoomsActivity::class.java)
            startActivity(intent)
        }

//        binding.cardMyBookings.setOnClickListener {
//            val intent = Intent(this, MyBookingsActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.cardSearchRooms.setOnClickListener {
//            val intent = Intent(this, SearchRoomsActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.cardBookRoom.setOnClickListener {
//            val intent = Intent(this, BookRoomActivity::class.java)
//            startActivity(intent)
//        }
    }
}