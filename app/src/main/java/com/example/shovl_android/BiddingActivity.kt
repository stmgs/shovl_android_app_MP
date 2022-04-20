package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.data.Post
import com.example.shovl_android.data.Users
import com.example.shovl_android.databinding.ActivityBiddingBinding
import com.example.shovl_android.databinding.ActivityRegisterBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.google.firebase.firestore.FirebaseFirestore
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FieldValue

class BiddingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBiddingBinding
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBiddingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.extras?.get("post_data_from_details") as Post
        val postedBy = intent.extras?.get("posted_by_details") as Users
        preferenceMangager= PreferenceMangager(this)


        binding.proceed.setOnClickListener {
            val price = binding.price.text.toString()
            val time = binding.time.text.toString()
            if(price.isNullOrEmpty())
            {
                Toast.makeText(this, "Please Enter Valid Price", Toast.LENGTH_SHORT).show()
            }else if(time.isNullOrEmpty()){
                Toast.makeText(this, "Please Enter Valid Time", Toast.LENGTH_SHORT).show()
            }else {
                val db = FirebaseFirestore.getInstance()
                val bidding = hashMapOf<String, Any>(
                    ShovlConstants.KEY_BID_PRICE to price,
                    ShovlConstants.KEY_REQ_TIME to time
                )

                val bidderMap= hashMapOf<String, Any>(
                    "user_id" to preferenceMangager.getString(ShovlConstants.KEY_USER_ID),
                    "name" to preferenceMangager.getString(ShovlConstants.KEY_NAME),
                    "price" to price.toInt(),
                    "time" to time.toInt()
                )


                db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                    .document(post.id.toString())
                    .update(ShovlConstants.KEY_BIDDERS,FieldValue
                        .arrayUnion(bidderMap))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bid has been posted.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }.addOnFailureListener {
                        loading(false)
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    }

                //add data to firebase
                /*db.collection(ShovlConstants.KEY_COLLECTION_BIDDING)
                    .add(bidding)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bid has been posted.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("fire error", it.message.toString())
                        loading(false)
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    }*/
            }
        }

        val cancel = findViewById(R.id.cancel) as Button
        cancel.setOnClickListener {
            finish()
           // startActivity(Intent(this, HomeActivity::class.java))

        }

    }
    private fun loading(isLoading : Boolean){
        if (isLoading){
            binding.proceed.visibility= View.VISIBLE
            binding.cancel.visibility= View.GONE
        }else{
            binding.proceed.visibility= View.GONE
            binding.cancel.visibility= View.VISIBLE
        }
    }
}