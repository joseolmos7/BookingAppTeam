package com.example.bookingappteam

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.bookingappteam.databinding.ActivityAvailableRoomsBinding
import com.google.firebase.database.*

class AvailableRoomsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAvailableRoomsBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvailableRoomsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference.child("rooms")

        fetchRooms()

        binding.btnBook.setOnClickListener {
            val selectedRoomId = binding.radioGroupRooms.checkedRadioButtonId
            if (selectedRoomId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedRoomId)
                val selectedRoomName = selectedRadioButton.tag as String
                // TODO: Implement booking logic
                Toast.makeText(this, "Booking logic for $selectedRoomName to be implemented", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select a room", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRooms() {
        database.get().addOnSuccessListener { snapshot ->
            for (roomSnapshot in snapshot.children) {
                val roomName = roomSnapshot.key
                val isAvailable = roomSnapshot.child("available").getValue(Boolean::class.java)
                val capacity = roomSnapshot.child("capacity").getValue(Long::class.java)

                if (roomName != null && isAvailable == true) {
                    val radioButton = RadioButton(this)
                    val styledText = "<b>$roomName</b><br>&nbsp;&nbsp;&nbsp;<small><i>capacity $capacity students</i></small>"
                    radioButton.text = HtmlCompat.fromHtml(styledText, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    radioButton.tag = roomName
                    binding.radioGroupRooms.addView(radioButton)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to fetch rooms", Toast.LENGTH_SHORT).show()
        }
    }
}