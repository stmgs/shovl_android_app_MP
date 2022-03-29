package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
<<<<<<< HEAD
import com.example.shovl_android.databinding.ActivityMainBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceMangager = PreferenceMangager(applicationContext)

        binding.tvSignOut.setOnClickListener {
            preferenceMangager.clear()

            startActivity(Intent(this, LoginActivity::class.java))
        }
=======
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bidding = findViewById(R.id.bidding) as Button
        bidding.setOnClickListener({startActivity(Intent(this, BiddingActivity::class.java))})

        val adlisting = findViewById(R.id.adlisting) as Button
        adlisting.setOnClickListener({startActivity(Intent(this, AdListingActivity::class.java))})
>>>>>>> 3cb676c93c52023e213322b09ffe35a59b53c389
    }


}