package com.example.level5task2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.capstoneassignmentjack.model.Reward

@Database(entities = [Reward::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RewardRoomDatabase : RoomDatabase() {

    abstract fun rewardDao(): RewardDao

    companion object {
        private const val DATABASE_NAME = "REWARD_DATABASE"

        @Volatile
        private var INSTANCE: RewardRoomDatabase? = null

        fun getDatabase(context: Context): RewardRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(RewardRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            RewardRoomDatabase::class.java, DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}