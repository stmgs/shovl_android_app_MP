package com.example.shovl_android.data

import java.io.Serializable

data class Users(
    var id : String? = null,
    val address : String?= null ,
    val age : Int?= null,
    val name : String?= null,
    val phone : String?= null,
    val gender : String?= null,
    val email : String?= null,
) : Serializable