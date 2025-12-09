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
        // Inflate the menu into the topAppBar
        binding.topAppBar.inflateMenu(R.menu.top_app_bar_menu)

        // Handle menu item clicks
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        binding.cardMyBookings.setOnClickListener {
            val intent = Intent(this, MyBookingsActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        binding.cardProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        binding.cardSearchRooms.setOnClickListener {
            val intent = Intent(this, SearchRoomsActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        binding.cardHelpSupport.setOnClickListener {
            val intent = Intent(this, HelpSupportActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }


    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}