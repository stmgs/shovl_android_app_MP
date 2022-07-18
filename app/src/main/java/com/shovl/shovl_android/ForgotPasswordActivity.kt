package com.shovl.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.shovl.shovl_android.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitForgot.setOnClickListener {
            val email = binding.etEmailForgot.text.toString()

            if (email.isNullOrEmpty()){
                binding.textInputEmailForgot.error = "Email is empty"
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.textInputEmailForgot.error = "Invalid email"

            }else{
                val auth = FirebaseAuth.getInstance()

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        Toast.makeText(this, "An email is sent to reset your password. " +
                                "Please check your email ",Toast.LENGTH_LONG).show()

                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    .addOnFailureListener {

                    }
            }
        }
    }



}