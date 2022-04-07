package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.data.Post
import com.example.shovl_android.databinding.ActivityBiddingBinding
import com.example.shovl_android.databinding.ActivityRegisterBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.google.firebase.firestore.FirebaseFirestore
import com.example.shovl_android.utilities.ShovlConstants

class BiddingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBiddingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBiddingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = intent.extras?.get("post_data_from_details") as Post



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

                //add data to firebase
                db.collection(ShovlConstants.KEY_COLLECTION_BIDDING)
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
                    }
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