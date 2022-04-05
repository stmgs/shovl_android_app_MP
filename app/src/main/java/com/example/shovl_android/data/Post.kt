package com.example.shovl_android.data

data class Post(
    val id : String? = null,
    val address : String?= null,
    val title : String?= null,
    val description : String?= null,
    val date_from : String?= null,
    val date_to : String?= null,
    val time_From : String?= null,
    val time_to : String?= null,
    val images : ArrayList<String>?= null
)
