package com.example.ass_3
// MainActivity.kt
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskInput: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var prioritySpinner: Spinner
    private lateinit var addButton: Button

    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskInput = findViewById(R.id.task_input)
        dateButton = findViewById(R.id.date_button)
        timeButton = findViewById(R.id.time_button)
        prioritySpinner = findViewById(R.id.priority_spinner)
        addButton = findViewById(R.id.add_button)

        // Set up the priority spinner
        val priorities = arrayOf("High", "Medium", "Low")
        prioritySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, priorities)

        // Set up the date picker
        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateButton.text = selectedDate
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set up the time picker
        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    selectedTime = "$hourOfDay:$minute"
                    timeButton.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            ).show()
        }

        // Send data to the second activity
        addButton.setOnClickListener {
            // Add this code inside your addButton.setOnClickListener
            val taskTitle = taskInput.text.toString()
            val priority = prioritySpinner.selectedItem.toString()
            val taskDetails = "$taskTitle - $selectedDate $selectedTime (Priority: $priority)"

// Retrieve existing tasks from the intent or create a new list
            val tasksList = intent.getStringArrayListExtra("tasksList") ?: arrayListOf()
            tasksList.add(taskDetails)

            val intent = Intent(this, TaskListActivity::class.java).apply {
                putExtra("tasksList", tasksList)
            }
            startActivity(intent)

// Clear inputs as before
            taskInput.text.clear()
            prioritySpinner.setSelection(0)
            selectedDate = ""
            selectedTime = ""

        }
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tasklist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_task -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent) // Start TaskListActivity when "View Tasks" is selected
                true
            }
            R.id.menu_view_tasks -> {

                val intent = Intent(this, TaskListActivity::class.java)
                startActivity(intent) // Start TaskListActivity when "View Tasks" is selected
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
