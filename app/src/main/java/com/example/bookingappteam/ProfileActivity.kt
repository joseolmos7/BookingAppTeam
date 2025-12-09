package com.example.bookingappteam

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappteam.databinding.ActivityProfileBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.BarcodeFormat
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase DB reference
        database = FirebaseDatabase.getInstance().getReference("users")

        val username = intent.getStringExtra("USERNAME")

        if (!username.isNullOrEmpty()) {
            database.orderByChild("username").equalTo(username).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val fullName = userSnapshot.child("fullName").value.toString()
                        binding.tvFullName.text = fullName

                        // --- Avatar handling ---
                        val ivUserImage: ShapeableImageView = binding.ivUserImage
                        when (username) {
                            "jose123" -> ivUserImage.setImageResource(R.drawable.jose)
                            //"javi123" -> ivUserImage.setImageResource(R.drawable.javi)
                            else -> ivUserImage.setImageResource(R.drawable.five_works)
                        }

                        // Generate barcode with full name encoded
                        generateBarcode(fullName)
                    }
                }
            }
        }

        // Current date in red
        val sdf = SimpleDateFormat("MMM d, yyyy h:mm:ssa", Locale.getDefault())
        val currentDate = sdf.format(Date())
        binding.tvDate.text = currentDate
        binding.tvDate.setTextColor(Color.RED)
    }

    private fun generateBarcode(data: String) {
        try {
            val barcodeEncoder = BarcodeEncoder()

            // Generate a 14-digit numeric code FIRST
            val numericCode = (1..14)
                .map { (0..9).random() }   // pick random digits
                .joinToString("")          // join into a string

            // Encode the numeric code into the barcode
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(numericCode, BarcodeFormat.CODE_128, 600, 200)
            binding.ivBarcode.setImageBitmap(bitmap)

            // Show 14 numbers below the barcode
            binding.tvBarCode.text = numericCode

        } catch (e: Exception) {
            binding.tvBarCode.text = "Error generating barcode"
        }
    }
}