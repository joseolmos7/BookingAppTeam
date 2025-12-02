package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappteam.databinding.ActivityEquiptmentViewsBinding

class EquiptmentViews : AppCompatActivity() {
    private lateinit var binding : ActivityEquiptmentViewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquiptmentViewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEquipmentBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }


    }
}