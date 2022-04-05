package com.example.shovl_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shovl_android.databinding.ActivityHomeBinding
import com.example.shovl_android.fragments.AddPostFragment
import com.example.shovl_android.fragments.ConfirmFragment
import com.example.shovl_android.fragments.PostsFragment
import com.example.shovl_android.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    fun changeFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.host_fragment, fragment)
            commit()
        }
    }
}