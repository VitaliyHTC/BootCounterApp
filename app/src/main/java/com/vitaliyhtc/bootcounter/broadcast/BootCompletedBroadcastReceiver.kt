package com.vitaliyhtc.bootcounter.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.vitaliyhtc.bootcounter.application.BootCounterApp
import com.vitaliyhtc.bootcounter.application.BootCounterApp.Companion.LOG_TAG
import com.vitaliyhtc.bootcounter.data.DataStore
import com.vitaliyhtc.bootcounter.data.room.BroadcastEvent

class BootCompletedBroadcastReceiver : BroadcastReceiver() {

    private val dataStore: DataStore = BootCounterApp.instance.getDataStore()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return

        Log.e(LOG_TAG, "BootCompletedBroadcastReceiver.kt onReceive: action: ${intent.action}")
        if (intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_REBOOT
        ) {
            val broadcastEvent = BroadcastEvent.parseIntent(intent)
            Log.e(
                LOG_TAG,
                "BootCompletedBroadcastReceiver.kt event: ${broadcastEvent.toShortString()}."
            )

            dataStore.saveBroadcastEvent(broadcastEvent)
        }
    }
}
