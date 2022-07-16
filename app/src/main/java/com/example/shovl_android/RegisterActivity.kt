package com.example.shovl_android

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shovl_android.databinding.ActivityRegisterBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
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
    private lateinit var phone:String

    private lateinit var encodedImage : String
    private lateinit var preferenceMangager: PreferenceMangager

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)

        auth = FirebaseAuth.getInstance()

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.ivPpRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }

        binding.btnRegister.setOnClickListener {
            email=binding.etEmailSignup.text.toString()
            password=binding.etPasswordSignup.text.toString()
            confirmPassword=binding.etConfirmPasswordSignup.text.toString()
            val ageString = binding.etAgeSignup.text.toString()
            if (ageString.isEmpty()){
                binding.textInputAgeSignup.error="Age is empty"
                binding.textInputAgeSignup.isErrorEnabled = true
            }else{
                age=binding.etAgeSignup.text.toString().toInt()

            }
            name=binding.etNameSignup.text.toString()
            address = binding.etAddressSignup.text.toString()
            gender=getGenderValue()
            phone=binding.etPhoneSignup.text.toString()

            if (!validateEmail(email)|| !validatePassword(password) || !validateConfirmPassword(password,confirmPassword)
                || !validateAge(age)){
            }else{
                loading(true)
                val db=FirebaseFirestore.getInstance()
                val user= hashMapOf<String, Any>(
                    ShovlConstants.KEY_EMAIL to email,
                    ShovlConstants.KEY_NAME to name,
                    ShovlConstants.KEY_AGE to age,
                    ShovlConstants.KEY_ADDRESS to address,
                    ShovlConstants.KEY_GENDER to gender,
                    ShovlConstants.KEY_PHONE to phone,
                    ShovlConstants.KEY_DP_IMAGE to encodedImage
                )

                /*create user in authentication tab*/
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Account Created.",Toast.LENGTH_SHORT).show()
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
                                    preferenceMangager.putString(ShovlConstants.KEY_PHONE, phone)
                                    preferenceMangager.putString(ShovlConstants.KEY_DP_IMAGE, encodedImage)

                                    val intent = Intent(this, HomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Log.d("fire error", it.message.toString())
                                    loading(false)
                                    Toast.makeText(this, it.message.toString(),Toast.LENGTH_LONG).show()
                                    val createdUser = FirebaseAuth.getInstance().currentUser
                                    createdUser?.delete()
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d("user del", "User account deleted.")
                                            }
                                        }

                                }
                        } else {
                            loading(false)
                            Toast.makeText(this,"Cannot create account", Toast.LENGTH_SHORT).show()
                        }
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

    private fun encodeImage(bitmap : Bitmap) : String{
        val previewWidth:Int = 150;
        val previewheight:Int = bitmap.height*previewWidth / bitmap.width
        val previewBitmap : Bitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewheight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , byteArrayOutputStream )
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)

    }

    val pickImage : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result->

            if (result.resultCode == RESULT_OK){
                if (result.data !=null){
                    val imageUri = result.data!!.data
                    try {
                        val inputStream = contentResolver.openInputStream(imageUri!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.ivPpRegister.setImageBitmap(bitmap)
                        binding.tvAddImageText.visibility=View.GONE
                        encodedImage=encodeImage(bitmap)

                    } catch (e:FileNotFoundException){
                        e.printStackTrace()
                    }
                }
            }

    }



}