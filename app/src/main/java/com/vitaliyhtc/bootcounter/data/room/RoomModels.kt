package com.vitaliyhtc.bootcounter.data.room

import android.content.Intent
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.vitaliyhtc.bootcounter.utils.DateTimeFormatUtils
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Entity(tableName = "broadcastEvents")
data class BroadcastEvent(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "action") val action: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) {

    fun toShortString(): String {
        val time = DateTimeFormatUtils.toDateTimeString(timestamp)
        return "$action at $time;"
    }

    companion object {

        fun parseIntent(intent: Intent): BroadcastEvent {
            return BroadcastEvent(
                uid = UUID.randomUUID().toString(),
                action = intent.action,
                timestamp = System.currentTimeMillis()
            )
        }
    }
}

@Dao
interface BroadcastEventDao {
    @Query("SELECT * FROM broadcastEvents")
    fun getAllAndSubscribe(): Flow<List<BroadcastEvent>>

    @Query("SELECT * FROM broadcastEvents")
    suspend fun getAll(): List<BroadcastEvent>

    @Query("SELECT * FROM broadcastEvents WHERE uid IN (:eventIds)")
    fun loadAllByIds(eventIds: Array<String>): Flow<List<BroadcastEvent>>

    @Insert
    suspend fun insertAll(events: List<BroadcastEvent>)

    @Delete
    suspend fun delete(event: BroadcastEvent)

    @Query("DELETE FROM broadcastEvents")
    suspend fun deleteAllBroadcastEvents()
}
