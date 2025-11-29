package com.example.bookingappteam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Intent
import com.example.bookingappteam.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(email.isNotEmpty() && password.isNotEmpty()){
                validateLogin(email, password)
            } else{
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLoginAdmin.setOnClickListener { // Added this block
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLogin(inputEmail: String, inputPassword: String){
        database.child("users").orderByChild("username").equalTo(inputEmail).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                var loginSuccess = false
                for (userSnapshot in snapshot.children) {
                    val password = userSnapshot.child("password").value as? String
                    if (password == inputPassword) {
                        loginSuccess = true
                        break
                    }
                }

                if (loginSuccess) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("USERNAME", inputEmail)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this, "Database error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}