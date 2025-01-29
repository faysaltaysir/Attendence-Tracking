package com.cse.smartattendence20

import android.app.Activity
import android.content.Intent
import android.net.wifi.hotspot2.pps.HomeSp
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val signUpRedirectText = findViewById<TextView>(R.id.signUpRedirectText)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            when {
                email.isEmpty() -> {
                    emailEditText.error = "Email cannot be empty"
                }

                !isValidEmail(email) -> {
                    emailEditText.error = "Invalid email format"
                }

                password.isEmpty() -> {
                    passwordEditText.error = "Password cannot be empty"
                }

                else -> {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    // Add navigation or other logic here

//                    loginButton.setOnClickListener {
//                        val intent = Intent(this, HomePage::class.java)
//                        startActivity(intent)
//                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000) // 3 seconds delay
                        // Navigate to HomePage after delay
                        val intent = Intent(this@LoginPage, TeacherProfile::class.java)
                        startActivity(intent)
                        finish()  // Finish the current activity to remove it from the back stack
                    }
                }
            }
        }
        // Add this OnClickListener for the SignUp TextView
        signUpRedirectText.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
