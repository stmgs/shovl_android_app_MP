package com.example.shovl_android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shovl_android.R
import com.example.shovl_android.databinding.FragmentProfileBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants

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
        preferenceMangager = PreferenceMangager(requireContext())
        binding.addressEditText1.text = preferenceMangager.getString(ShovlConstants.KEY_ADDRESS)
        binding.nameEditText.text = preferenceMangager.getString(ShovlConstants.KEY_NAME)
        binding.genderEditText.text = preferenceMangager.getString(ShovlConstants.KEY_GENDER)
        binding.ageEditText.text = preferenceMangager.getInt(ShovlConstants.KEY_AGE).toString()



    }

}