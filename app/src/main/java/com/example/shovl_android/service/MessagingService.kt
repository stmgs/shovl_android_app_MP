package com.example.shovl_android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.shovl_android.HomeActivity
import com.example.shovl_android.R
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID="General"

class MessagingService : FirebaseMessagingService() {

    private lateinit var preferenceMangager: PreferenceMangager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferenceMangager = PreferenceMangager(applicationContext)
        preferenceMangager.putString(ShovlConstants.KEY_FCM_TOKEN, token.toString())

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val intent = Intent(this, HomeActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificaiton = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["message"])
            .setSmallIcon(R.drawable.ic_brightness)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notificaiton)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager : NotificationManager){
        val channelName="general"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH )
            .apply {
                description = "This is a general notification channel"
                enableLights(true)
                lightColor= Color.BLUE
        }
        notificationManager.createNotificationChannel(channel)
    }
}