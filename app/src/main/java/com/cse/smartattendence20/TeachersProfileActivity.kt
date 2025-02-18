package com.cse.smarty

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cse.smarty.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class TeachersProfileActivity : AppCompatActivity() {

    private lateinit var spinnerDepartment: Spinner
    private lateinit var spinnerCourse: Spinner
    private lateinit var studentListContainer: LinearLayout
    private lateinit var tvStudentList: TextView
    private lateinit var btnSetDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnViewProfile: Button

    // Simulated data: Map of departments to courses, and courses to students
    private val departmentCourseStudents = mapOf(
        "Computer Science" to mapOf(
            "Math 101" to listOf("2004001", "2004002", "2004003", "2004004", "2004005", "2004006", "2004007", "2004008", "2004009", "2004010"),
            "Physics 202" to listOf("2004011", "2004012", "2004013", "2004014", "2004015", "2004016", "2004017", "2004018", "2004019", "2004020")
        ),
        "Electrical Engineering" to mapOf(
            "Chemistry 202" to listOf("2004021", "2004022", "2004023", "2004024", "2004025", "2004026", "2004027", "2004028", "2004029", "2004030")
        )
    )

    // Data structure to store attendance records: Map<Date, Map<StudentRoll, AttendanceStatus>>
    private val attendanceRecords = mutableMapOf<String, MutableMap<String, String>>()

    // Selected date for attendance
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teachers_profile)

        spinnerDepartment = findViewById(R.id.spinner_department)
        spinnerCourse = findViewById(R.id.spinner_course)
        studentListContainer = findViewById(R.id.student_list_container)
        tvStudentList = findViewById(R.id.tv_student_list)
        btnSetDate = findViewById(R.id.btn_set_date)
        tvSelectedDate = findViewById(R.id.tv_selected_date)
        btnViewProfile = findViewById(R.id.btn_view_profile)

        btnViewProfile.setOnClickListener {
            val intent = Intent(this, StudentProfileActivity::class.java)
            startActivity(intent)
        }
        // Load saved attendance records
        loadAttendanceRecords()

        // Populate department spinner
        val departments = listOf("Select Department", "Computer Science", "Electrical Engineering")
        spinnerDepartment.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, departments)

        // Handle department selection
        spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) { // Ignore first default selection
                    val selectedDepartment = spinnerDepartment.selectedItem as String
                    populateCourses(selectedDepartment)
                } else {
                    // Clear course spinner if no department is selected
                    spinnerCourse.adapter = ArrayAdapter(this@TeachersProfileActivity, android.R.layout.simple_spinner_dropdown_item, listOf("Select Course"))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle course selection
        spinnerCourse.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) { // Ignore first default selection
                    loadStudentList()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle date selection
        btnSetDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate.text = "Selected Date: $selectedDate"
                tvSelectedDate.visibility = View.VISIBLE

                // Load attendance for the selected date
                loadStudentList()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun populateCourses(department: String) {
        // Get courses for the selected department
        val courses = departmentCourseStudents[department]?.keys?.toList() ?: emptyList()

        // Populate course spinner
        val courseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOf("Select Course") + courses)
        spinnerCourse.adapter = courseAdapter
    }

    private fun loadStudentList() {
        studentListContainer.removeAllViews() // Clear previous list
        tvStudentList.visibility = View.VISIBLE
        studentListContainer.visibility = View.VISIBLE

        // Check if a date is selected
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show()
            return
        }

        // Get selected department and course
        val selectedDepartment = spinnerDepartment.selectedItem as String
        val selectedCourse = spinnerCourse.selectedItem as String

        // Fetch students for the selected department and course
        val students = departmentCourseStudents[selectedDepartment]?.get(selectedCourse) ?: emptyList()

        // Get attendance records for the selected date
        val attendanceMap = attendanceRecords[selectedDate] ?: mutableMapOf()

        // Display students
        for (roll in students) {
            val studentRow = TextView(this).apply {
                text = roll
                textSize = 18f
                setTextColor(Color.parseColor("#333333")) // Dark Gray Text
                setPadding(16, 16, 16, 16)
                gravity = android.view.Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(10, 10, 10, 10)
                }

                // Set background based on attendance status
                val attendanceStatus = attendanceMap[roll]
                if (attendanceStatus == "Present") {
                    setBackgroundColor(Color.parseColor("#A3E635")) // Green for Present
                } else {
                    setBackgroundColor(Color.parseColor("#FFDDC1")) // Warm Peach for Absent
                }
            }

            // Handle click to toggle attendance (Present/Absent)
            studentRow.setOnClickListener {
                val attendanceStatus = attendanceMap[roll]
                if (attendanceStatus == "Present") {
                    studentRow.setBackgroundColor(Color.parseColor("#FFDDC1")) // Reset to default
                    attendanceMap.remove(roll) // Remove from attendance records
                } else {
                    studentRow.setBackgroundColor(Color.parseColor("#A3E635")) // Green for Present
                    attendanceMap[roll] = "Present" // Mark as Present
                }

                // Save updated attendance records
                attendanceRecords[selectedDate] = attendanceMap
                saveAttendanceRecords()
            }

            studentListContainer.addView(studentRow)
        }
    }

    // Save attendance records to SharedPreferences
    private fun saveAttendanceRecords() {
        val sharedPreferences = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(attendanceRecords) // Convert map to JSON
        editor.putString("attendance_data", json) // Save JSON string
        editor.apply()
    }

    // Load attendance records from SharedPreferences
    private fun loadAttendanceRecords() {
        val sharedPreferences = getSharedPreferences("AttendancePrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("attendance_data", null)
        val type = object : TypeToken<MutableMap<String, MutableMap<String, String>>>() {}.type
        if (json != null) {
            attendanceRecords.clear()
            attendanceRecords.putAll(gson.fromJson(json, type)) // Convert JSON to map
        }
    }
}