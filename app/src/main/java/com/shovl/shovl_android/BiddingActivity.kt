package com.shovl.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.shovl.shovl_android.data.NotificationData
import com.shovl.shovl_android.data.Post
import com.shovl.shovl_android.data.PushNotification
import com.shovl.shovl_android.data.Users
import com.shovl.shovl_android.databinding.ActivityBiddingBinding
import com.shovl.shovl_android.utilities.PreferenceMangager
import com.google.firebase.firestore.FirebaseFirestore
import com.shovl.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class BiddingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBiddingBinding
    private lateinit var preferenceMangager: PreferenceMangager
    val TAG="BiddingActivity"

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

                val bidderMap= hashMapOf<String, Any>(
                    "user_id" to preferenceMangager.getString(ShovlConstants.KEY_USER_ID),
                    "name" to preferenceMangager.getString(ShovlConstants.KEY_NAME),
                    "rejected" to false,
                    "price" to price.toInt(),
                    "time" to time.toInt()
                )

                db.collection(ShovlConstants.KEY_COLLECTION_POSTS)
                    .document(post.id.toString())
                    .update(ShovlConstants.KEY_BIDDERS, FieldValue.arrayUnion(bidderMap))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Bid has been posted.", Toast.LENGTH_SHORT).show()

                        PushNotification(
                            NotificationData("Bidder alert!!!"
                                    , "${preferenceMangager.getString(ShovlConstants.KEY_NAME)} bid on your post."
                                , null, null, "bidder"),
                            postedBy.fcm_token.toString()
                        ).also {
                                sendNotification(it)
                            }

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

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                //Log.d(TAG,"Response: ${Gson().toJson(response)}")
            }
        } catch (e: Exception){
            Log.e(TAG, e.toString())
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