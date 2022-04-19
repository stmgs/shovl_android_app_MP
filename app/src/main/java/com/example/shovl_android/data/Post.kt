package com.example.shovl_android.data

import java.io.Serializable

data class Post(
    var id : String? = null,
    var posted_by : String? = null,
    val address : String?= null,
    val title : String?= null,
    val description : String?= null,
    val date_from : String?= null,
    val date_to : String?= null,
    val time_From : String?= null,
    val time_to : String?= null,
    val images : ArrayList<String>?= null
) : Serializable
