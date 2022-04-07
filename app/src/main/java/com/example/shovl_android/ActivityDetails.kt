package com.example.shovl_android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.shovl_android.R
import com.example.shovl_android.adapters.SliderAdapter
import com.example.shovl_android.data.Post
import com.example.shovl_android.databinding.ActivityDetails2Binding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class ActivityDetails : AppCompatActivity() {

    private lateinit var binding : ActivityDetails2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.extras?.get("post_data") as Post
        println("post inside details activity $post")
        val imageList = post.images

        binding.tvDescriptionDetails.text=post.description
        binding.tvLocation.text=post.address
        binding.adTvStart.text=post.date_from
        binding.adTvEnd.text=post.date_to


        val adapter = SliderAdapter(imageList)

        with(binding.imageSliderDetails){
            setSliderAdapter(adapter)
            setIndicatorAnimation(IndicatorAnimationType.SWAP)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH

        }
        binding.btnApplyDetails.setOnClickListener {
            val intent = Intent(this, BiddingActivity::class.java)
            intent.putExtra("post_data_from_details", post)
            startActivity(intent)
        }
    }
}