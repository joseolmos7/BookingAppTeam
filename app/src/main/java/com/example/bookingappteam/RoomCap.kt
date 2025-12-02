package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.bookingappteam.databinding.ActivityRoomCapBinding

class RoomCap : AppCompatActivity() {
    private lateinit var binding : ActivityRoomCapBinding
    private var selectedCapacity : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoomCapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRoomBack.setOnClickListener {
            finish()
        }
    }
    private fun handleCapacitySelected(capacity: String){
        selectedCapacity = capacity
    }
}