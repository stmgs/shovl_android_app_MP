package com.shovl.shovl_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shovl.shovl_android.databinding.ActivityChoreDetailsBinding

class ChoreDetails : AppCompatActivity() {
    private lateinit var binding : ActivityChoreDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val amount= intent.getStringExtra("amount")
        val location = intent.getStringExtra("location")

        binding.tvAmount.text=amount
        binding.tvLocation.text=location



    }
}