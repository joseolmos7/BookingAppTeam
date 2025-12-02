package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappteam.databinding.ActivityPurposeViewBinding

class PurposeView : AppCompatActivity() {
    private lateinit var binding : ActivityPurposeViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPurposeViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPutposeBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }
}