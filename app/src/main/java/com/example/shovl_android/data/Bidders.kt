package com.example.shovl_android.data

import java.io.Serializable

data class Bidders(
    var user_id : String? = null,
    var name : String? = null,
    val price : Int?= null,
    val time : Int?= null,
) : Serializable
