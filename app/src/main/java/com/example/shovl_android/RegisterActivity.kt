package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import com.example.shovl_android.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var email : String
    private lateinit var password:String
    private lateinit var confirmPassword:String
    private lateinit var name:String
    private  var age:Int = 0
    private lateinit var address:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            email=binding.etEmailSignup.text.toString()
            password=binding.etPasswordSignup.text.toString()
            confirmPassword=binding.etConfirmPasswordSignup.text.toString()
            age=binding.etAgeSignup.text.toString().toInt()
            if (!validateEmail(email)|| !validatePassword(password) || !validateConfirmPassword(password,confirmPassword)
                || !validateAge(age)){

            }else{
                Log.d("reg","validation is added")
            }
        }

    }

    private fun validateEmail(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputEmailSignup.error = null
            binding.textInputEmailSignup.isErrorEnabled = false
            return true

        } else {
            binding.textInputEmailSignup.error = "Oops! Seems like you entered invalid email."
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
            binding.textInputPasswordSignup.setError("Password cannot be empty")
            return false
        } else if(password.length <8){
            binding.textInputPasswordSignup.setError("Password must have at least 8 characters")
            return false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            binding.textInputPasswordSignup.setError("Password must contain 1 special character and 1 digit")
            return false
        }  else {
            binding.textInputPasswordSignup.setError(null)
            binding.textInputPasswordSignup.isErrorEnabled = false
            return true
        }

    }

    private fun validateConfirmPassword(pw:String, confirmPw:String) : Boolean{
        if (!confirmPw.equals(pw)){
            binding.textInputConfirmPasswordSignup.error ="Passwords do not match"
            binding.textInputConfirmPasswordSignup.isErrorEnabled = true
            return true
        }else{
            binding.textInputConfirmPasswordSignup.error = null
            binding.textInputConfirmPasswordSignup.isErrorEnabled = false
            return true
        }
    }

    private fun validateAge(age:Int):Boolean{
        if (age<=18 || age>50){
            binding.textInputAgeSignup.error="You must be 18-50 years to register"
            binding.textInputAgeSignup.isErrorEnabled = true
            return false
        } else{
            binding.textInputAgeSignup.error=null
            binding.textInputAgeSignup.isErrorEnabled = false
            return true
        }
    }

}