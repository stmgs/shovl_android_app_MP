package com.example.shovl_android

import android.widget.Button
import android.widget.TextView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.R
import com.example.shovl_android.data.Post

class ActivityDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details2)

        val post = intent.extras?.get("post_data") as Post
        println("post inside details activity $post")


    }
}