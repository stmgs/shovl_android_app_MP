package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ViewProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        val editButton = findViewById<View>(R.id.editButton) as Button
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            // start your next activity
            startActivity(intent)

        }
    }
}