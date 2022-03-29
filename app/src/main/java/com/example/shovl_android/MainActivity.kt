package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }


}