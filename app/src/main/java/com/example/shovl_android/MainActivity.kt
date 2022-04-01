package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shovl_android.databinding.ActivityLoginBinding
import com.example.shovl_android.databinding.ActivityMainBinding
import com.example.shovl_android.utilities.PreferenceMangager

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)

//        val bidding = findViewById(R.id.bidding) as Button
        binding.bidding.setOnClickListener({startActivity(Intent(this, BiddingActivity::class.java))})

//        val adlisting = findViewById(R.id.adlisting) as Button
        binding.adlisting.setOnClickListener({startActivity(Intent(this, AdListingActivity::class.java))})
    }
}