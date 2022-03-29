package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bidding = findViewById(R.id.bidding) as Button
        bidding.setOnClickListener({startActivity(Intent(this, BiddingActivity::class.java))})

        val adlisting = findViewById(R.id.adlisting) as Button
        adlisting.setOnClickListener({startActivity(Intent(this, AdListingActivity::class.java))})
    }
}