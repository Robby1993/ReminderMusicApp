package com.robinson.reminderapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.robinson.reminderapp.util.MusicControl.Companion.getInstance
import java.util.Objects

class StopReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Objects.requireNonNull(intent.action)
                .equals("StopReminderReceiver", ignoreCase = true)
        ) {
            getInstance(context)!!.stopMusic()
            val id = intent.getIntExtra("NOTIFICATION_ID",0)
            NotificationManagerCompat.from(context).cancel(id)

        }
    }
}