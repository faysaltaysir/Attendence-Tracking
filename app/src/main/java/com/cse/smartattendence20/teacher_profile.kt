package com.cse.smartattendence20

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class TeacherProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile)

        // Define the dropdown menu lists
        val list1 = listOf("CSE 1", "CSE 2", "CSE 3")
        val list2 = listOf("Option B1", "Option B2", "Option B3")
        val list3 = listOf("Option C1", "Option C2", "Option C3")
        val list4 = listOf("Option D1", "Option D2", "Option D3")

        // Initialize spinners and set adapters
        setupSpinner(findViewById(R.id.spinner1), list1)
    }

    // Helper function to set up spinners
    private fun setupSpinner(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        spinner.adapter = adapter
    }
}
