package com.example.capstoneassignmentjack.popupdatabase

import android.content.Context
import androidx.compose.ui.window.Popup
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.capstoneassignmentjack.model.Popup
import com.example.level5task2.database.Converters

@Database(entities = [Popup::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PopupRoomDatabase : RoomDatabase() {

    abstract fun popupDao(): PopupDao

    companion object {
        private const val DATABASE_NAME = "POPUP_DATABASE"

        @Volatile
        private var INSTANCE: PopupRoomDatabase? = null

        fun getDatabase(context: Context): PopupRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(PopupRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            PopupRoomDatabase::class.java, DATABASE_NAME
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