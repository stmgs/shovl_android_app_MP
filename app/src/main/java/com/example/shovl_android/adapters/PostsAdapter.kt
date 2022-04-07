package com.example.shovl_android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shovl_android.R
import com.example.shovl_android.data.Post

class PostsAdapter(private val postList: ArrayList<Post>,
                   viewClickListener: PostRVClickListener
) :
    RecyclerView.Adapter<PostViewHolder>() {

    private val listener: PostRVClickListener? = viewClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.rv_post_item,parent,false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        with(holder){
            with(postList[position]){
                Glide.with(postIv)
                    .load(images?.get(0))
                    .centerCrop()
                    .placeholder(R.drawable.grey_tint_background)
                    .error(R.drawable.image_not_available)
                    .fallback(R.drawable.image_not_available)
                    .into(postIv)
                titleTv.text=this.title
                locationTv.text=this.address

            }
        }
        holder.postIv.setOnClickListener {
            if (listener != null) {
                listener.onVClick(postList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return   postList.size }

    interface PostRVClickListener {
        fun onVClick(post: Post)
    }

    }

class PostViewHolder(itemView : View)
    : RecyclerView.ViewHolder(itemView) {
    val postIv = itemView.findViewById<ImageView>(R.id.iv_rv_posts)
    val titleTv = itemView.findViewById<TextView>(R.id.tv_rv_title_posts)
    val locationTv=itemView.findViewById<TextView>(R.id.tv_rv_location_posts)

}
