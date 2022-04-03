package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.databinding.ActivityBiddingBinding
import com.example.shovl_android.databinding.ActivityRegisterBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.google.firebase.firestore.FirebaseFirestore
import com.example.shovl_android.utilities.ShovlConstants

class BiddingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBiddingBinding
    private lateinit var preferenceMangager: PreferenceMangager

    var imageList = intArrayOf(
        R.drawable.snow_image_1,
        R.drawable.snow_image_2,
        R.drawable.snow_image_3,
        R.drawable.snow_image_4
    ) // DEMO IMAGES

    var count = imageList.size
    var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBiddingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)

        val btn_next = findViewById<Button>(R.id.btn_next) as Button
        val btn_pre = findViewById<Button>(R.id.btn_pre) as Button
        val img_scroller = findViewById<ImageSwitcher>(R.id.img_scroller) as ImageSwitcher

        img_scroller.setFactory {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.layoutParams = FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT
            )
            imageView
        }

        img_scroller.setImageResource(imageList[0])

        btn_pre.setOnClickListener {
            img_scroller.setInAnimation(
                this@BiddingActivity,
                R.anim.from_right
            )
            img_scroller.setOutAnimation(
                this@BiddingActivity,
                R.anim.to_left
            )
            --currentIndex
            if (currentIndex < 0) currentIndex = imageList.size - 1
            img_scroller.setImageResource(imageList[currentIndex])
        }

        btn_next.setOnClickListener {
            img_scroller.setInAnimation(
                this@BiddingActivity,
                R.anim.from_left
            )
            img_scroller.setOutAnimation(
                this@BiddingActivity,
                R.anim.to_right
            )
            currentIndex++
            if (currentIndex == count) currentIndex = 0
            img_scroller.setImageResource(imageList[currentIndex])
        }

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
                        Log.d("success", "data stored")
                        preferenceMangager.putString(ShovlConstants.KEY_BID_PRICE, price)
                        preferenceMangager.putString(ShovlConstants.KEY_REQ_TIME, time)

                        val intent = Intent(this, MainActivity::class.java)
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
        cancel.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
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