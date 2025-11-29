package com.example.bookingappteam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val btnManageBookings = findViewById<Button>(R.id.btnManageBookings)
        btnManageBookings.setOnClickListener {
            startActivity(Intent(this, ManageBookingActivity::class.java))
        }
    }
}