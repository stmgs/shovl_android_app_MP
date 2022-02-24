package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.databinding.ActivitySplashscreenBinding


class Splashscreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed(Runnable { //This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            // close this activity
            finish()
        }, 2000)
    }
}