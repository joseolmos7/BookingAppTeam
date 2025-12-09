package com.example.bookingappteam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappteam.databinding.ActivityHelpSupportBinding

class HelpSupportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpSupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}