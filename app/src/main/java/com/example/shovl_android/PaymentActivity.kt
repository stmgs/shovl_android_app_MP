package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shovl_android.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)








//        binding.btnPay.setOnClickListener {
//            Toast.makeText(this, "Payment is made.", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, HomeActivity::class.java))
//            finish()
//        }
    }
}