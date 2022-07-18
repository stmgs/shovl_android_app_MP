package com.shovl.shovl_android.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.shovl.shovl_android.R

class ImagesRvAdapterAdList(private val picsUrls: ArrayList<Uri>) :
    RecyclerView.Adapter<ImagesRvAdapterAdList.PicsViewholder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PicsViewholder {
        return PicsViewholder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_images_ad_listing, parent, false))
    }

    override fun onBindViewHolder(holder: ImagesRvAdapterAdList.PicsViewholder, position: Int) {
        holder.bind(picsUrls, position)
    }

    override fun getItemCount(): Int {
        return picsUrls.size
    }

    class PicsViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView=itemView.findViewById<ImageView>(R.id.rv_item_ad_list_iv_image)

        fun bind(picsUrls: ArrayList<Uri>, position: Int) {
            imageView.setImageURI(picsUrls[position])
        }
    }
}