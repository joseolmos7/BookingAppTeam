package com.example.bookingappteam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.firestore.FirebaseFirestore


import android.content.Intent
import com.example.bookingappteam.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}