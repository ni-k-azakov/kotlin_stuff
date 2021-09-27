package com.example.glassgo

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val intentToShow = Intent(context, LaunchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intentToShow, 0)
        val builder = NotificationCompat.Builder(context, "waterNotifications")
                .setSmallIcon(R.drawable.blob)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_des))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(200, builder.build())
        }
    }
}