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
import com.example.shovl_android.PaymentActivity
import com.example.shovl_android.adapters.ConfirmShovelerAdapter
import com.example.shovl_android.data.Bidders
import com.example.shovl_android.data.Post
import com.example.shovl_android.databinding.FragmentConfirmBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ConfirmFragment : Fragment() {

    private lateinit var binding : FragmentConfirmBinding
    private lateinit var preferenceMangager: PreferenceMangager

    var postList = ArrayList<Post>()
    var postModel: Post = Post()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceMangager = PreferenceMangager(requireContext())

        val db= FirebaseFirestore.getInstance()
        db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
            .whereEqualTo(ShovlConstants.POSTED_BY,preferenceMangager.getString(ShovlConstants.KEY_USER_ID))
            .get()
            .addOnSuccessListener { it ->
                if (it.isEmpty){
                    //if block is working fine
                    binding.tvNoDataPosts.visibility=View.VISIBLE
                } else{
                    binding.tvNoDataPosts.visibility=View.GONE

                    postList.clear()
                    for (postDocumentFromFirestore in it){
                        postModel =postDocumentFromFirestore.toObject(Post::class.java)
                        postModel.id = postDocumentFromFirestore.id
                        postList.add(postModel)
                    }

                    binding.rvConfirmShoveler.also {recyclerview->
                        recyclerview.layoutManager= LinearLayoutManager(requireContext()
                            , LinearLayoutManager.VERTICAL, false)
                        recyclerview.setHasFixedSize(true)
                        recyclerview.adapter =
                            postModel.bidders?.let { it1 ->
                                ConfirmShovelerAdapter(it1, object : ConfirmShovelerAdapter.ConfirmRVClickListener{
                                    override fun onConfirmClicked(bidder: Bidders) {
                                        val intent = Intent(requireContext(), PaymentActivity::class.java)
                                        intent.putExtra("bidder_data", bidder)
                                        startActivity(intent)
                                    }

                                    override fun onDeleteClicked(bidder: Bidders) {
                                        it1.remove(bidder)

                                        val bidderMapToDelete= hashMapOf<String, Any?>(
                                            "user_id" to bidder.user_id,
                                            "name" to bidder.name,
                                            "price" to bidder.price,
                                            "time" to bidder.time
                                        )

                                        db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                                            .document(postModel.bidders.toString())
                                            .update(ShovlConstants.KEY_BIDDERS,
                                                FieldValue.arrayRemove(bidderMapToDelete))
                                            .addOnSuccessListener {

                                            }
                                            .addOnFailureListener {

                                            }


                                        binding.rvConfirmShoveler.adapter?.notifyDataSetChanged()

                                    }

                                })
                            }
                    }


                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


    }

}