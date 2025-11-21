package com.example.bookingappteam

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance()

        // 🔧 Sample room data
        val room = hashMapOf(
            "roomNumber" to "101",
            "type" to "Deluxe",
            "isAvailable" to true,
            "pricePerNight" to 149.99,
            "features" to listOf("WiFi", "TV", "Mini Fridge")
        )

        db.collection("rooms")
            .add(room)
            .addOnSuccessListener { docRef ->
                Log.d("FIREBASE", "Room added with ID: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FIREBASE", "Error adding room", e)
            }
    }
}