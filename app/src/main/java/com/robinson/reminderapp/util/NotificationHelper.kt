package com.robinson.reminderapp.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.robinson.reminderapp.MainActivity
import com.robinson.reminderapp.R
import java.util.concurrent.atomic.AtomicInteger


open class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "reminder_channel_id"
    private var NOTIFICATION_ID = 1

    fun createNotification(title: String, message: String) {
        NOTIFICATION_ID = notificationID()
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val snoozeButton = Intent(context, StopReminderReceiver::class.java)
        snoozeButton.putExtra("NOTIFICATION_ID", NOTIFICATION_ID)
        snoozeButton.action = "StopReminderReceiver"
        snoozeButton.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = getPendingIntent(intent)
        // Create the reply action and add the remote input.
        val action = NotificationCompat.Action.Builder(
            R.drawable.reminder_icon,
            "Stop", getBroadcastPendingIntent(snoozeButton)
        ).build()

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.reminder_icon)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.reminder_icon)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(icon))
            .setContentIntent(pendingIntent)
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    protected fun getPendingIntent(notificationIntent: Intent?): PendingIntent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }


    protected fun getBroadcastPendingIntent(snoozeButton: Intent): PendingIntent? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                snoozeButton,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                NOTIFICATION_ID,
                snoozeButton,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminder Channel Description"
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun notificationID(): Int {
        val c = AtomicInteger(0)
        return c.incrementAndGet()
    }


}