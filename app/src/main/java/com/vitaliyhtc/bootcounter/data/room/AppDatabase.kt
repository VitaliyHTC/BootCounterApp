package com.vitaliyhtc.bootcounter.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vitaliyhtc.bootcounter.application.BootCounterApp
import kotlinx.coroutines.flow.Flow

@Database(entities = [BroadcastEvent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun broadcastEventsDao(): BroadcastEventDao
}

class BootCounterDatabase {

    private lateinit var db: AppDatabase
    private lateinit var broadcastEventDao: BroadcastEventDao

    fun setup() {
        val applicationContext = BootCounterApp.instance
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            APP_DATABASE_NAME
        ).build()

        broadcastEventDao = db.broadcastEventsDao()
    }

    fun getAllBroadcastEventsAndSubscribe(): Flow<List<BroadcastEvent>> {
        return broadcastEventDao.getAllAndSubscribe()
    }

    suspend fun getAllBroadcastEvents(): List<BroadcastEvent> {
        return broadcastEventDao.getAll()
    }

    suspend fun addAllBroadcastEvents(events: List<BroadcastEvent>) {
        return broadcastEventDao.insertAll(events)
    }

    suspend fun deleteBroadcast(event: BroadcastEvent) {
        broadcastEventDao.delete(event)
    }

    suspend fun deleteAllBroadcastEvents() {
        broadcastEventDao.deleteAllBroadcastEvents()
    }

    companion object {
        private const val APP_DATABASE_NAME = "boot_counter_database"
    }
}