package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shovl_android.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var email : String
    private lateinit var password:String
    val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //if (Patterns.EMAIL_ADDRESS.matcher(username).matches() )
        binding.tvNewUser.setOnClickListener {
            startActivity(Intent(this, ActivityDetails::class.java))
        }

        binding.btnLogin.setOnClickListener {
            email= binding.etEmail.text.toString()
            password=binding.etPassword.text.toString()

            if (!validateEmail(email) || !validatePassword(password)){
                //Toast.makeText(this,"Invalid email or password", Toast.LENGTH_LONG).show()
            }else{
                val user= hashMapOf(
                    "email" to "sajantmg2@gmail.com",
                    "password" to "sajantamang"
                )

                //Add document to firestore
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener {
                        Log.d("success", "data stored")
                    }
                    .addOnFailureListener {
                        Log.d("fire error", it.message.toString())
                    }

                //startActivity(Intent(this, AdListingActivity::class.java))
                //finish()
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputEmail.error = null
            binding.textInputEmail.isErrorEnabled = false
            return true

        } else {
            binding.textInputEmail.error = "Oops! Seems like you entered invalid email."
            return false
        }
    }

    private fun validatePassword(password: String): Boolean {
        val PASSWORD_PATTERN =
            Pattern.compile(
                "^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        //"(?=\\S+$)" +           //no white spaces
                        ".{8,}" +               //minimum 8 characters
                        "$"

            )

        // if password field is empty
        // it will display error message "Password can not be empty"
        if (password.isEmpty()){
            binding.textInputPassword.setError("Password cannot be empty")
            return false
        } else if(password.length <8){
            binding.textInputPassword.setError("Password must have at least 8 characters")
            return false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            binding.textInputPassword.setError("Password must contain 1 special character and 1 digit")
            return false
        }  else {
            binding.textInputPassword.setError(null)
            binding.textInputPassword.isErrorEnabled = false
            return true
        }

    }

}