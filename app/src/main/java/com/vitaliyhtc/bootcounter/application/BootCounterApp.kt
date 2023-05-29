package com.vitaliyhtc.bootcounter.application

import android.app.Application
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.vitaliyhtc.bootcounter.data.DataStore
import com.vitaliyhtc.bootcounter.data.room.BootCounterDatabase
import com.vitaliyhtc.bootcounter.notifications.NotificationManager
import com.vitaliyhtc.bootcounter.worker.BroadcastEventNotificationWorker
import com.vitaliyhtc.bootcounter.worker.BroadcastEventNotificationWorker.Companion.BROADCAST_EVENT_NOTIFICATION_WORKER_NAME
import com.vitaliyhtc.bootcounter.worker.BroadcastEventNotificationWorker.Companion.BROADCAST_EVENT_NOTIFICATION_WORKER_PERIODIC_INTERVAL_MINUTES
import java.util.concurrent.TimeUnit

class BootCounterApp : Application() {

    private val bootCounterDatabase = BootCounterDatabase()
    private val dataStore = DataStore(bootCounterDatabase)

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        Log.e(LOG_TAG, "BootCounterApp.kt onCreate()")

        setupApplication()
        setupWorkManager()
    }

    private fun setupApplication() {
        bootCounterDatabase.setup()

        NotificationManager.checkNotificationChannelsConfig()
    }

    private fun setupWorkManager() {
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<BroadcastEventNotificationWorker>(
                BROADCAST_EVENT_NOTIFICATION_WORKER_PERIODIC_INTERVAL_MINUTES,
                TimeUnit.MINUTES
            )
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            BROADCAST_EVENT_NOTIFICATION_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    fun getDataStore() = dataStore

    companion object {
        private val TAG = BootCounterApp::class.java.simpleName
        const val LOG_TAG = "_energy"

        lateinit var instance: BootCounterApp
    }
}