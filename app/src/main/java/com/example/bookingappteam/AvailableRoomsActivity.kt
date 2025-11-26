package com.example.bookingappteam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappteam.databinding.ActivityAvailableRoomsBinding
class AvailableRoomsActivity : AppCompatActivity() {
    private lateinit var b : ActivityAvailableRoomsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAvailableRoomsBinding.inflate(layoutInflater)
        setContentView(b.root)



    }
}