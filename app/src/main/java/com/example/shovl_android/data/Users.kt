package com.example.shovl_android.data

data class Users(
    val id : String,
    val address : String,
    val age : Int,
    val name : String,
    val phone : String,
    val gender : String,
    val email : String,
    val imageList : ArrayList<String>
)