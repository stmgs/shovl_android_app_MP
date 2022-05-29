package com.example.shovl_android.service

import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    private lateinit var preferenceMangager: PreferenceMangager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferenceMangager = PreferenceMangager(applicationContext)
        preferenceMangager.putString(ShovlConstants.KEY_FCM_TOKEN, token.toString())

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        println("the remore fcm message is :" +remoteMessage.notification)

    }
}