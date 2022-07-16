package com.example.shovl_android

import com.example.shovl_android.data.PushNotification
import com.example.shovl_android.utilities.ShovlConstants
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=${ShovlConstants.FIREBASE_SERVER_KEY}"
        ,"Content-Type:${ShovlConstants.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification : PushNotification
    ) : Response<ResponseBody>
}

