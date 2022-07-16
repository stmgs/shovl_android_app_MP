package com.example.shovl_android

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shovl_android.databinding.ActivityEditProfileBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class EditProfile : AppCompatActivity() {

    private lateinit var preferenceMangager: PreferenceMangager
    private lateinit var binding : ActivityEditProfileBinding
    private var encodedImage : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceMangager = PreferenceMangager(this)

        binding.addressEditText1.hint = preferenceMangager.getString(ShovlConstants.KEY_ADDRESS)
        binding.nameEditText.hint = preferenceMangager.getString(ShovlConstants.KEY_NAME)
        binding.ageEditText.hint = preferenceMangager.getInt(ShovlConstants.KEY_AGE).toString()

        binding.ivPpEdit.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }

        binding.updateButton.setOnClickListener {
            var address = binding.addressEditText1.text.toString()
            if (address.isNullOrEmpty())
                address = preferenceMangager.getString(ShovlConstants.KEY_ADDRESS)

            var name = binding.nameEditText.text.toString()
            if (name.isNullOrEmpty())
                name = preferenceMangager.getString(ShovlConstants.KEY_NAME)

            var gender = getGenderValue()
            if (gender.isNullOrEmpty())
                gender = preferenceMangager.getString(ShovlConstants.KEY_GENDER)

            var age = binding.ageEditText.text.toString()
            var ageInt =preferenceMangager.getInt(ShovlConstants.KEY_AGE)

            if (!age.isNullOrEmpty())
                ageInt = age.toInt()

            encodedImage=preferenceMangager.getString(ShovlConstants.KEY_DP_IMAGE)

            if (encodedImage.isNullOrEmpty())
                encodedImage=""

            val user= hashMapOf<String, Any>(
                ShovlConstants.KEY_EMAIL to preferenceMangager.getString(ShovlConstants.KEY_EMAIL),
                ShovlConstants.KEY_NAME to name,
                ShovlConstants.KEY_AGE to ageInt,
                ShovlConstants.KEY_ADDRESS to address,
                ShovlConstants.KEY_GENDER to gender,
                ShovlConstants.KEY_PHONE to preferenceMangager.getString(ShovlConstants.KEY_PHONE),
                ShovlConstants.KEY_DP_IMAGE to encodedImage!!
            )

            val db= FirebaseFirestore.getInstance()

            db.collection(ShovlConstants.KEY_COLLECTION_USERS)
                .document(preferenceMangager.getString(ShovlConstants.KEY_USER_ID))
                .set(user)
                .addOnSuccessListener {
                    preferenceMangager.putBoolean(ShovlConstants.KEY_IS_SIGNED_IN, true)
                    preferenceMangager.putString(ShovlConstants.KEY_NAME, name)
                    preferenceMangager.putString(ShovlConstants.KEY_GENDER, gender)
                    preferenceMangager.putString(ShovlConstants.KEY_ADDRESS, address)
                    preferenceMangager.putInt(ShovlConstants.KEY_AGE, ageInt)
                    preferenceMangager.putString(ShovlConstants.KEY_DP_IMAGE, encodedImage!!)

                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Log.d("fire error", it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
        }

        binding.cancelButton.setOnClickListener {
            finish()

        }
    }

    private fun getGenderValue(): String {
        return if (binding.rgGender.checkedRadioButtonId == binding.rbMale.id) {
            "Male"
        } else {
            "Female"
        }
    }

    val pickImage : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result->

        if (result.resultCode == RESULT_OK){
            if (result.data !=null){
                val imageUri = result.data!!.data
                try {
                    val inputStream = contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.ivPpEdit.setImageBitmap(bitmap)
                    binding.tvAddImageTextEdit.visibility=View.GONE
                    encodedImage=encodeImage(bitmap)

                } catch (e: FileNotFoundException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun encodeImage(bitmap : Bitmap) : String{
        val previewWidth:Int = 150;
        val previewheight:Int = bitmap.height*previewWidth / bitmap.width
        val previewBitmap : Bitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewheight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50 , byteArrayOutputStream )
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)

    }
}