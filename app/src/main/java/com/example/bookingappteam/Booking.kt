package com.example.bookingappteam

import com.google.firebase.database.DatabaseReference

data class Booking(
    val roomName: String,
    val date: String,
    val time: String,
    val purpose: String,
    val numberOfStudents: String,
    val username: String,
    val dbRef: DatabaseReference? = null,
    var isSelected: Boolean = false
)