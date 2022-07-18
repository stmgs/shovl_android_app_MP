package com.example.shovl_android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.shovl_android.databinding.ActivitySplashscreenBinding
import com.example.shovl_android.utilities.PreferenceMangager
import com.example.shovl_android.utilities.ShovlConstants


class Splashscreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashscreenBinding
    private lateinit var preferenceMangager: PreferenceMangager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceMangager = PreferenceMangager(applicationContext)



        binding.btnInfo.setOnClickListener {
            startActivity(Intent(this, TeamInformationPage::class.java))
        }


        if (intent.extras != null) {

            when (intent.extras!!.get("type")) {
                "chore"->{
                    val i = Intent(baseContext, ChoreDetails::class.java)
                    i.putExtra("amount", intent.extras!!.getString("amount"))
                    i.putExtra("location", intent.extras!!.getString("location"))
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)

                }

                "bidder"->{

                }
                /*"Interested" -> {
                    val pendingIntent = NavDeepLinkBuilder(this)
                        .setGraph(R.navigation.main_navigation)
                        .setDestination(R.id.searchProfileFragment)
                        .setArguments(
                            SearchPersonProfileFragmentArgs(
                                intent.extras!!.getString("username")
                                    .toString()
                            ).toBundle()
                        )
                        .setComponentName(MainActivity::class.java)
                        .createPendingIntent()

                    startActivity(Intent())

                    val notification = NotificationCompat.Builder(this, "my_channel")
                        .setContentTitle(intent.extras!!.getString("title"))
                        .setContentText(intent.extras!!.getString("body"))
                        //.setLargeIcon(bitmap)
                        .setSmallIcon(R.drawable.nepali_vivah_logo)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                        //.setShowWhen(true)
                        .build()

                    val notificationManager = NotificationManagerCompat.from(this)
                    // Remove prior notifications; only allow one at a time to edit the latest item
                    //notificationManager.cancelAll()
                    notificationManager.notify(1, notification)

                    *//* val i = Intent(baseContext, SearchPersonProfileActivity::class.java)
                     i.putExtra("user_name", intent.extras!!.getString("username"))
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                     startActivity(i)*//*

                }*/


            }

        } else {
            if (preferenceMangager.getBoolean(ShovlConstants.KEY_IS_SIGNED_IN)){
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            } else{
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}