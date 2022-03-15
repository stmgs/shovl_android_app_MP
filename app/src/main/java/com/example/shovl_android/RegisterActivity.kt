package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.shovl_android.databinding.ActivityRegisterBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var email : String
    private lateinit var password:String
    private lateinit var confirmPassword:String
    private lateinit var name:String
    private  var age:Int = 18
    private lateinit var address:String
    private lateinit var gender:String

    private lateinit var encodedImage : String
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnRegister.setOnClickListener {
            email=binding.etEmailSignup.text.toString()
            password=binding.etPasswordSignup.text.toString()
            confirmPassword=binding.etConfirmPasswordSignup.text.toString()
            var ageString = binding.etAgeSignup.text.toString()
            if (ageString.isNullOrEmpty()){
                binding.textInputAgeSignup.error="Age is empty"
                binding.textInputAgeSignup.isErrorEnabled = true
            }else{
                age=binding.etAgeSignup.text.toString().toInt()

            }
            name=binding.etNameSignup.text.toString()
            address = binding.etAddressSignup.text.toString()
            gender=getGenderValue()
            println("gender from function $gender")

            if (!validateEmail(email)|| !validatePassword(password) || !validateConfirmPassword(password,confirmPassword)
                || !validateAge(age)){
                println()
            }else{

                Log.d("reg","validation is added")
                loading(true)
                val db=FirebaseFirestore.getInstance()
                val user= hashMapOf<String, Any>(
                    ShovlConstants.KEY_EMAIL to email,
                    ShovlConstants.KEY_PASSWORD to password,
                    ShovlConstants.KEY_NAME to name,
                    ShovlConstants.KEY_AGE to age,
                    ShovlConstants.KEY_ADDRESS to address,
                    ShovlConstants.KEY_GENDER to gender
                )

                //add user to firestore
                db.collection(ShovlConstants.KEY_COLLECTION_USERS)
                    .add(user)
                    .addOnSuccessListener {
                        Log.d("success", "data stored")
                        preferenceMangager.putBoolean(ShovlConstants.KEY_IS_SIGNED_IN, true)
                        preferenceMangager.putString(ShovlConstants.KEY_USER_ID, it.id)
                        preferenceMangager.putString(ShovlConstants.KEY_EMAIL, email)
                        preferenceMangager.putString(ShovlConstants.KEY_NAME, name)
                        preferenceMangager.putString(ShovlConstants.KEY_GENDER, gender)
                        preferenceMangager.putString(ShovlConstants.KEY_ADDRESS, address)
                        preferenceMangager.putInt(ShovlConstants.KEY_AGE, age)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("fire error", it.message.toString())
                        Toast.makeText(this, it.message.toString(),Toast.LENGTH_LONG).show()
                    }
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
        if (password.isNullOrEmpty()){
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
        if (age<18 || age>50){
            binding.textInputAgeSignup.error="You must be 18-50 years to register"
            binding.textInputAgeSignup.isErrorEnabled = true
            return false
        }  else{
            binding.textInputAgeSignup.error=null
            binding.textInputAgeSignup.isErrorEnabled = false
            return true
        }
    }

    private fun loading(isLoading : Boolean){
        if (isLoading){
            binding.pbSignup.visibility=View.VISIBLE
            binding.btnRegister.visibility=View.GONE
        }else{
            binding.pbSignup.visibility=View.GONE
            binding.btnRegister.visibility=View.VISIBLE
        }
    }

    private fun getGenderValue(): String {
        return if (binding.rgGender.checkedRadioButtonId == binding.rbMale.id) {
            "Male"
        } else {
            "Female"
        }
    }

}