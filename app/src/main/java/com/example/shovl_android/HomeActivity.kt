package com.example.shovl_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.shovl_android.databinding.ActivityHomeBinding
import com.example.shovl_android.fragments.AddPostFragment
import com.example.shovl_android.fragments.ConfirmFragment
import com.example.shovl_android.fragments.PostsFragment
import com.example.shovl_android.fragments.ProfileFragment
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    private lateinit var preferenceMangager: PreferenceMangager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(this    )

        getToken()

        val postsFragment = PostsFragment()
        val addPostFragment = AddPostFragment()
        val confirmFragment = ConfirmFragment()
        val profileFragment = ProfileFragment()

        changeFragment(postsFragment)

        binding.bottomNav.setOnItemSelectedListener {menuitem->
            when(menuitem.itemId){
                R.id.navigation_post -> changeFragment(postsFragment)
                R.id.navigation_add -> changeFragment(addPostFragment)
                R.id.navigation_confirm -> changeFragment(confirmFragment)
                R.id.navigation_profile -> changeFragment(profileFragment)
            }
            true
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            updateToken(it)}
    }


    private fun updateToken(token : String) {
        val db= FirebaseFirestore.getInstance()
        db.collection(ShovlConstants.KEY_COLLECTION_USERS)
            .document(preferenceMangager.getString(ShovlConstants.KEY_USER_ID))
            .update(ShovlConstants.KEY_FCM_TOKEN, token)
            .addOnSuccessListener {
                preferenceMangager.putString(ShovlConstants.KEY_FCM_TOKEN, token)
            }

    }



    fun changeFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.host_fragment, fragment)
            commit()
        }
    }
}