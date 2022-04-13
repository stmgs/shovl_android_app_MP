package com.example.shovl_android.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shovl_android.ActivityDetails
import com.example.shovl_android.adapters.PostsAdapter
import com.example.shovl_android.data.Post
import com.example.shovl_android.databinding.FragmentPostsBinding
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FirebaseFirestore

class PostsFragment : Fragment() {
    private lateinit var binding : FragmentPostsBinding
    var postList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db= FirebaseFirestore.getInstance()
        db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty){
                    //if block is working fine
                    binding.tvNoDataPosts.visibility=View.VISIBLE
                } else{
                    binding.tvNoDataPosts.visibility=View.GONE

                    postList.clear()
                    for (postDocumentFromFirestore in it){

                        val postModel =postDocumentFromFirestore.toObject(Post::class.java)
                        postModel.id = postDocumentFromFirestore.id
                        postList.add(postModel)

                    }

                    binding.rvPosts.also {
                        it.layoutManager= StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
                        it.setHasFixedSize(true)
                        it.adapter =
                            PostsAdapter(postList, object : PostsAdapter.PostRVClickListener{

                                override fun onVClick(post: Post) {
                                    val intent = Intent(requireContext(), ActivityDetails::class.java)
                                    intent.putExtra("post_data", post)
                                    startActivity(intent)

                                }

                            })
                    }


                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }




    }

}