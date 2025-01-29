package com.cse.smartattendence20

import android.app.Activity;
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

public class RegisterPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        val emailEditText = findViewById<EditText>(R.id.signup_email)
        val passwordEditText = findViewById<EditText>(R.id.signup_password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.signup_confirm)
        val signUpButton = findViewById<Button>(R.id.signup_button)
        val loginRedirectText = findViewById<TextView>(R.id.loginRedirectText)

        val minPasswordLength = resources.getInteger(R.integer.min_password_length)
        val minLetters = resources.getInteger(R.integer.min_letters)
        val minNumeric = resources.getInteger(R.integer.min_numeric)
        val minSymbols = resources.getInteger(R.integer.min_symbols)
        val minNonLetter = resources.getInteger(R.integer.min_nonletter)
        val maxPasswordLength = resources.getInteger(R.integer.max_password_length)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            when {
                !isValidEmail(email) -> {
                    emailEditText.error = "Invalid Email"
                }

                password.length < minPasswordLength || password.length > maxPasswordLength -> {
                    passwordEditText.error =
                        "Password must be between $minPasswordLength and $maxPasswordLength characters"
                }

                password.contains(" ") -> {
                    passwordEditText.error = "Password must not contain spaces"
                }

                password != confirmPassword -> {
                    confirmPasswordEditText.error = "Passwords do not match"
                }

                !hasValidPasswordComplexity(
                    password, minLetters, minNumeric, minSymbols, minNonLetter
                ) -> {
                    passwordEditText.error =
                        "Password must contain letters, numbers, symbols, and non-letter characters"
                }

                else -> {
                    Toast.makeText(this, "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                    // Navigate to LoginActivity or other logic
                    val intent = Intent(this, LoginPage::class.java)
                    startActivity(intent)
                    finish() // Close the current activity
                }
            }
        }


        // Add this OnClickListener for the Login TextView
        loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun hasValidPasswordComplexity(
        password: String, minLetters: Int, minNumeric: Int, minSymbols: Int, minNonLetter: Int
    ): Boolean {
        var letters = 0
        var numeric = 0
        var symbols = 0
        var nonLetters = 0

        for (ch in password) {
            when {
                ch.isLetter() -> letters++
                ch.isDigit() -> numeric++
                !ch.isLetterOrDigit() -> symbols++
            }
            if (!ch.isLetter()) nonLetters++
        }

        return letters >= minLetters && numeric >= minNumeric && symbols >= minSymbols && nonLetters >= minNonLetter
    }
}