package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val cancelButton = findViewById<View>(R.id.cancelButton) as Button
        cancelButton.setOnClickListener {
            val intent = Intent(this, ViewProfile::class.java)
            // start your next activity
            startActivity(intent)

        }
    }
}