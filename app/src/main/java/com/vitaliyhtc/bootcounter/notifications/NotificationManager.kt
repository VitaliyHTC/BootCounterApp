package com.vitaliyhtc.bootcounter.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import com.vitaliyhtc.bootcounter.R
import com.vitaliyhtc.bootcounter.application.BootCounterApp

// TODO: add notification permission check
@Suppress("unused")
object NotificationManager {
    private const val MAIN_NOTIFICATION_CHANNEL_ID = "boot_counter_notifications_channel"

    private val context = BootCounterApp.instance
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var notificationIncrementalId: Int = 0

    fun checkNotificationChannelsConfig() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        checkMainNotificationChannelConfig()
    }

    fun deleteNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        notificationManager.deleteNotificationChannel(channelId)
    }

    fun openNotificationChannelSettings(channelId: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, BootCounterApp.instance.packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
        }
        startActivity(context, intent, null)
    }

    // =============================================================================================
    // Main notification channel
    // =============================================================================================

    private fun checkMainNotificationChannelConfig() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val notificationChannels = notificationManager.notificationChannels

        if (notificationChannels.find { it.id ==  MAIN_NOTIFICATION_CHANNEL_ID} == null) {
            val name = context.getString(R.string.notifications_channel_name)
            val descriptionText = context.getString(R.string.notifications_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MAIN_NOTIFICATION_CHANNEL_ID, name, importance)
            channel.enableVibration(false)
            channel.description = descriptionText
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showMainChannelNotification(title: String, content: String, notificationId: Int? = null) {
        val builder = NotificationCompat.Builder(context, MAIN_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_adb_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId ?: notificationIncrementalId++, builder.build())
    }
}