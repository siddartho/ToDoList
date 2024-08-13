
package com.example.ass_3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat

class TaskListActivity : AppCompatActivity() {
    private val REQUEST_NOTIFICATION_PERMISSION = 1001
    private lateinit var taskListView: ListView
    private val taskList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_list)

        taskListView = findViewById(R.id.task_list_view)

        // Retrieve tasks from the intent
        val tasksList = intent.getStringArrayListExtra("tasksList") ?: arrayListOf()

        // Display the tasks in the list
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasksList)
        taskListView.adapter = adapter

        // Request notification permission if needed
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION)
            } else {
                createNotificationChannel()
                if (tasksList.isNotEmpty()) {
                    sendNotification(tasksList.last())
                }
            }
        } else {
            createNotificationChannel()
            if (tasksList.isNotEmpty()) {
                sendNotification(tasksList.last())
            }
        }
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the notification
                sendNotification("Task Details")
            } else {
                // Permission denied, you can show a message to the user or handle it gracefully
                Log.d("TaskListActivity", "Notification permission denied.")
            }
        }

    }

    private fun sendNotification(taskDetails: String) {
        val builder = NotificationCompat.Builder(this, "TASK_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("New Task Added")
            .setContentText(taskDetails)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 1000))
            .setLights(0xFF00FF00.toInt(), 3000, 3000)
            .setAutoCancel(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build()) // Unique ID for each notification
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Channel"
            val descriptionText = "Channel for task notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("TASK_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
