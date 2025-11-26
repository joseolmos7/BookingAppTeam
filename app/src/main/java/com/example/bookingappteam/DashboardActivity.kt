package com.example.bookingappteam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


import android.content.Intent
import com.example.bookingappteam.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Is this the first time the app is launching? if yes show the fragment
        if not let the os handle it*/

        if (savedInstanceState == null){
            val fragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView2.id,fragment)
                .commit()
        }

        // Go to search rooms
        binding.btnSearch.setOnClickListener {
            val fragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView2.id,fragment)
                .commit()
        }

        // Go to History
        binding.btnHistory.setOnClickListener {
            val fragment = HistoryFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView2.id,fragment)
                .commit()
        }

        // Go to Help
        binding.btnHelp.setOnClickListener {
            val fragment = HelpFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView2.id,fragment)
                .commit()
        }

        // Go to Profile
        binding.btnProfile.setOnClickListener {
            val fragment = ProfileFragment()
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView2.id,fragment)
                .commit()
        }




    }
}