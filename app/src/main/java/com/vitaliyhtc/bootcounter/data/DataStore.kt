package com.vitaliyhtc.bootcounter.data

import android.util.Log
import com.vitaliyhtc.bootcounter.application.BootCounterApp.Companion.LOG_TAG
import com.vitaliyhtc.bootcounter.data.room.BootCounterDatabase
import com.vitaliyhtc.bootcounter.data.room.BroadcastEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataStore(private val appDataBase: BootCounterDatabase) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun saveBroadcastEvent(broadcastEvent: BroadcastEvent) {
        val ceh = CoroutineExceptionHandler { _, throwable ->
            Log.e(LOG_TAG, "DataStore failed to save broadcastEvent.", throwable)
        }
        scope.launch(ceh) {
            appDataBase.addAllBroadcastEvents(listOf(broadcastEvent))
        }
    }

    fun broadcastEventsUpdateListener(onEventsUpdated: (events: List<BroadcastEvent>) -> Unit) {
        val ceh = CoroutineExceptionHandler { _, throwable ->
            Log.e(LOG_TAG, "DataStore failed to get broadcastEvent.", throwable)
            onEventsUpdated.invoke(emptyList())
        }
        scope.launch(ceh) {
            appDataBase.getAllBroadcastEventsAndSubscribe().collect { events ->
                withContext(Dispatchers.Main) {
                    onEventsUpdated.invoke(events)
                }
            }
        }
    }

    fun getAllBroadcastEvents(onResult: (events: List<BroadcastEvent>) -> Unit) {
        val ceh = CoroutineExceptionHandler { _, throwable ->
            Log.e(LOG_TAG, "DataStore failed to get broadcastEvent.", throwable)
            onResult.invoke(emptyList())
        }
        scope.launch(ceh) {
            val events = appDataBase.getAllBroadcastEvents()
            withContext(Dispatchers.Main) {
                onResult.invoke(events)
            }
        }
    }

    suspend fun getAllBroadcastEvents(): List<BroadcastEvent> {
        return appDataBase.getAllBroadcastEvents()
    }

    fun resetBroadcastEvents() {
        val ceh = CoroutineExceptionHandler { _, throwable ->
            Log.e(
                LOG_TAG,
                "DataStore failed to delete all items from broadcastEvents table.",
                throwable
            )
        }
        scope.launch(ceh) {
            appDataBase.deleteAllBroadcastEvents()
        }
    }
}
