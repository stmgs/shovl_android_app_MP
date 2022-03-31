package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.shovl_android.databinding.ActivityMainBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceMangager: PreferenceMangager
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceMangager = PreferenceMangager(applicationContext)
        auth = FirebaseAuth.getInstance()

        binding.tvSignOut.setOnClickListener {
            preferenceMangager.clear()
            auth.signOut()

            startActivity(Intent(this, LoginActivity::class.java))
        }

        val bidding = findViewById(R.id.bidding) as Button
        bidding.setOnClickListener({ startActivity(Intent(this, BiddingActivity::class.java)) })

        val adlisting = findViewById(R.id.adlisting) as Button
        adlisting.setOnClickListener({ startActivity(Intent(this, AdListingActivity::class.java)) })
    }

}