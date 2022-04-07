package com.example.shovl_android.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.shovl_android.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.info_bottom_sheet,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnClose = view.findViewById<ImageView>(R.id.btn_close_info)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)

        btnClose.setOnClickListener {
            super.dismiss()
        }

        tvEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:shovlappmdev@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, "Send feedback"))
        }
    }

}
