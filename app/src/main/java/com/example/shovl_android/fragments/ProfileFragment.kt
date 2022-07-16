package com.example.shovl_android.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.shovl_android.EditProfile
import com.example.shovl_android.LoginActivity
import com.example.shovl_android.R
import com.example.shovl_android.databinding.FragmentProfileBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class ProfileFragment : Fragment() {

    private lateinit var preferenceMangager: PreferenceMangager
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarProfile.inflateMenu(R.menu.profile_frag_menu)

        binding.toolbarProfile.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.info_menu_item -> {
                    val infoFragmentBottomSheet = InfoFragment()
                    infoFragmentBottomSheet.show(childFragmentManager,"")
                    true
                }
                R.id.logout_menu_item -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Are you sure you want to logout?")
                        .setPositiveButton("Yes", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            logout()
                        }
                    })
                    .setNegativeButton("No", object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                        }
                    }).show()
                    //dialog.show()
                    true
                }
                else -> false
            }
        }

        preferenceMangager = PreferenceMangager(requireContext())
        binding.addressEditText1.text = preferenceMangager.getString(ShovlConstants.KEY_ADDRESS)
        binding.nameEditText.text = preferenceMangager.getString(ShovlConstants.KEY_NAME)
        binding.genderEditText.text = preferenceMangager.getString(ShovlConstants.KEY_GENDER)
        binding.ageEditText.text = preferenceMangager.getInt(ShovlConstants.KEY_AGE).toString()
        val bytes = Base64.decode(preferenceMangager.getString(ShovlConstants.KEY_DP_IMAGE), Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.ivPpProfile.setImageBitmap(bitmap)

        binding.editButton.setOnClickListener {
                val intent = Intent(requireContext(), EditProfile::class.java)
                // start your next activity
                startActivity(intent)

        }
    }

    private fun logout(){
        val database = FirebaseFirestore.getInstance();
        val docRef = database.collection(ShovlConstants.KEY_COLLECTION_USERS)
            .document(preferenceMangager.getString(ShovlConstants.KEY_USER_ID))

        val updates = HashMap<String, Any>()
        updates.put(ShovlConstants.KEY_FCM_TOKEN, FieldValue.delete())
        docRef.update(updates)
            .addOnSuccessListener {
                preferenceMangager.clear()
                Toast.makeText(requireContext(),
                    "You have been logged out.", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
    }

}