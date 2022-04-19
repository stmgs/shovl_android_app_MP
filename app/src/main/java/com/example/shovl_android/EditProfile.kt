package com.example.shovl_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.shovl_android.databinding.ActivityEditProfileBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfile : AppCompatActivity() {

    private lateinit var preferenceMangager: PreferenceMangager
    private lateinit var binding : ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceMangager = PreferenceMangager(this)

        binding.addressEditText1.hint = preferenceMangager.getString(ShovlConstants.KEY_ADDRESS)
        binding.nameEditText.hint = preferenceMangager.getString(ShovlConstants.KEY_NAME)
        binding.ageEditText.hint = preferenceMangager.getInt(ShovlConstants.KEY_AGE).toString()

        binding.updateButton.setOnClickListener {
            val address = binding.addressEditText1.text.toString()
            val name = binding.nameEditText.text.toString()
            val gender = getGenderValue()
            val age = binding.ageEditText.text.toString().toInt()

            preferenceMangager.putString(ShovlConstants.KEY_NAME, name)
            preferenceMangager.putString(ShovlConstants.KEY_GENDER, gender)
            preferenceMangager.putString(ShovlConstants.KEY_ADDRESS, address)
            preferenceMangager.putInt(ShovlConstants.KEY_AGE, age)

            val user= hashMapOf<String, Any>(
                ShovlConstants.KEY_EMAIL to preferenceMangager.getString(ShovlConstants.KEY_EMAIL),
                ShovlConstants.KEY_NAME to name,
                ShovlConstants.KEY_AGE to age,
                ShovlConstants.KEY_ADDRESS to address,
                ShovlConstants.KEY_GENDER to gender,
                ShovlConstants.KEY_PHONE to preferenceMangager.getString(ShovlConstants.KEY_PHONE)
            )

            val db= FirebaseFirestore.getInstance()

            db.collection(ShovlConstants.KEY_COLLECTION_USERS)
                .document(preferenceMangager.getString(ShovlConstants.KEY_USER_ID))
                .set(user)
                .addOnSuccessListener {
                    Log.d("success", "data stored")
                    preferenceMangager.putBoolean(ShovlConstants.KEY_IS_SIGNED_IN, true)
                    preferenceMangager.putString(ShovlConstants.KEY_NAME, name)
                    preferenceMangager.putString(ShovlConstants.KEY_GENDER, gender)
                    preferenceMangager.putString(ShovlConstants.KEY_ADDRESS, address)
                    preferenceMangager.putInt(ShovlConstants.KEY_AGE, age)

                    finish()
                    /*val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)*/
                }
                .addOnFailureListener {
                    Log.d("fire error", it.message.toString())
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
        }

        binding.cancelButton.setOnClickListener {
            finish()
            /*val intent = Intent(this, ViewProfile::class.java)
            // start your next activity
            startActivity(intent)*/

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