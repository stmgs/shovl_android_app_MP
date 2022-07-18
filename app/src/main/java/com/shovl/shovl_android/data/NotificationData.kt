package com.shovl.shovl_android.data

data class NotificationData(
    val title:String,
    val message:String,
    //val body:HashMap<String, String>?
    val amount:String?,
    val location: String?,
    val type:String?
) {
}