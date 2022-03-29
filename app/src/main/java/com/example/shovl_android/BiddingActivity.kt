package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BiddingActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_bidding)

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

        val proceed = findViewById(R.id.proceed) as Button
        proceed.setOnClickListener({startActivity(Intent(this, MainActivity::class.java))})

        val cancel = findViewById(R.id.cancel) as Button
        cancel.setOnClickListener({startActivity(Intent(this, MainActivity::class.java))})
    }
}