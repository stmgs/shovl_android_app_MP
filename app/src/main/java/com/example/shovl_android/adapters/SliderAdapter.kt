package com.example.shovl_android.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.shovl_android.R
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*


class SliderAdapter(
    photosUrls: ArrayList<String>?
) : SliderViewAdapter<SliderAdapter.SliderAdapterVH?>() {
    val photosUrlsnew = photosUrls


    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout_item, null)
        return SliderAdapterVH(inflate)
    }


    override fun getCount(): Int {
        return photosUrlsnew?.size!!
    }

    inner class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {

        var imageViewBackground: ImageView

        init {
            imageViewBackground = itemView.findViewById(R.id.iv_profile_image)

        }
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) {

        val imageUrl = photosUrlsnew?.get(position)
        Glide.with(viewHolder!!.imageViewBackground )
            .load(imageUrl)
            .into(viewHolder!!.imageViewBackground)

    }


}