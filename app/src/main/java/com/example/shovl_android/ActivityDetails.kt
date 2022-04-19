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
import com.example.shovl_android.data.Users
import com.example.shovl_android.databinding.ActivityDetails2Binding
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class ActivityDetails : AppCompatActivity() {
    private lateinit var binding : ActivityDetails2Binding
    private lateinit var posterDetails : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetails2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.extras?.get("post_data") as Post
        val imageList = post.images

        val db= FirebaseFirestore.getInstance()
        db.collection(ShovlConstants.KEY_COLLECTION_USERS)
            .document(post.posted_by.toString())
            .get()
            .addOnSuccessListener {

                posterDetails = it.toObject(Users::class.java)!!
                posterDetails?.id=post.posted_by.toString()
                binding.tvPostedByDetails.text= "Posted by: "+posterDetails?.name ?: "XXX XXX"

            }.addOnFailureListener {

            }

        binding.tvDescriptionDetails.text=post.description
        binding.tvLocation.text=post.address
        binding.adTvStart.text=post.date_from
        binding.adTvEnd.text=post.date_to
        binding.tvTimeFromDetails.text=post.time_From
        binding.tvTimeToDetails.text=post.time_to


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
            intent.putExtra("posted_by_details", posterDetails)
            startActivity(intent)
        }
    }
}