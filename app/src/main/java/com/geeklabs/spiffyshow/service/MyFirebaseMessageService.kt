package com.geeklabs.spiffyshow.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.geeklabs.spiffyshow.App
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.PrefManager
import com.geeklabs.spiffyshow.utils.Utils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.log4k.d
import javax.inject.Inject
import kotlin.random.Random


class MyFirebaseMessageService : FirebaseMessagingService() {

    @Inject
    lateinit var prefManager: PrefManager

    override fun onCreate() {
        super.onCreate()
        (application as App).applicationComponent.inject(this)
    }

    override fun onNewToken(token: String) {
        d("Refreshed token: $token")
        prefManager.save(Constants.FIREBASE_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        d("From: " + remoteMessage.from)
        if (Utils.appIsInForeground(this) || !Utils.isServiceRunning(this)) {
            return
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title ?: ""
            val messageBody = remoteMessage.notification?.body ?: ""
            val intent = newLauncherIntent(remoteMessage.data)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            d("Message Notification Body: " + remoteMessage.notification?.body)
            val notification =
                NotificationCompat.Builder(this, FIREBASE_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(
                        (ContextCompat.getDrawable(
                            this,
                            R.drawable.app_logo
                        ) as BitmapDrawable).bitmap
                    )
                    .setSmallIcon(R.drawable.app_logo)
                    .setAutoCancel(true).build()

            val notificationId = Random.nextInt(1, Int.MAX_VALUE)
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, notification)
            }
        }
    }

    private fun newLauncherIntent(data: Map<String, String>): Intent {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        data.entries.forEach {
            intent.putExtra(it.key, it.value)
        }
        return intent
    }

    companion object {
        const val FIREBASE_CHANNEL_ID = "FIREBASE_CHANNEL_ID"
    }
}