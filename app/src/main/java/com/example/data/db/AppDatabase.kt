package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PitchEntity::class,
        BookingEntity::class,
        ChatMessageEntity::class,
        MatchPostEntity::class,
        PlayerStatsEntity::class,
        NotificationEntity::class,
        ManagerSlotEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pitchDao(): PitchDao
    abstract fun bookingDao(): BookingDao
    abstract fun chatDao(): ChatDao
    abstract fun matchPostDao(): MatchPostDao
    abstract fun playerStatsDao(): PlayerStatsDao
    abstract fun notificationDao(): NotificationDao
    abstract fun managerSlotDao(): ManagerSlotDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "takwira_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
