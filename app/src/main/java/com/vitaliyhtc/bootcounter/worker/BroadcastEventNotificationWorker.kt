package com.vitaliyhtc.bootcounter.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.vitaliyhtc.bootcounter.R
import com.vitaliyhtc.bootcounter.application.BootCounterApp
import com.vitaliyhtc.bootcounter.application.BootCounterApp.Companion.LOG_TAG
import com.vitaliyhtc.bootcounter.notifications.NotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BroadcastEventNotificationWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val bootCounterApp = BootCounterApp.instance
        val dataStore = bootCounterApp.getDataStore()

        val events = dataStore.getAllBroadcastEvents()

        Log.e(LOG_TAG, "BroadcastEventNotificationWorker.kt doWork() events.size: ${events.size}")

        // TODO: add events formatting message as needed by task.
        NotificationManager.showMainChannelNotification(
            bootCounterApp.getString(R.string.new_broadcast_event_received),
            bootCounterApp.getString(
                R.string.broadcast_notification_content,
                "${events.last().timestamp}"
            ),
            NOTIFICATION_ID
        )

        Result.success()
    }

    companion object {
        private const val NOTIFICATION_ID = 2

        const val BROADCAST_EVENT_NOTIFICATION_WORKER_NAME = "BroadcastEventNotificationWorker"

        const val BROADCAST_EVENT_NOTIFICATION_WORKER_PERIODIC_INTERVAL_MINUTES = 15L
    }
}
