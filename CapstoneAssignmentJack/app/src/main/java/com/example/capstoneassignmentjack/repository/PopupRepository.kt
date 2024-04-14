package com.example.capstoneassignmentjack.repository

import android.content.Context
import com.example.capstoneassignmentjack.model.Popup
import com.example.capstoneassignmentjack.model.Reward
import com.example.capstoneassignmentjack.popupdatabase.PopupDao
import com.example.capstoneassignmentjack.popupdatabase.PopupRoomDatabase
import com.example.level5task2.database.RewardDao
import com.example.level5task2.database.RewardRoomDatabase

class PopupRepository(context: Context) {
    private val popupDao: PopupDao

    init {
        val database = PopupRoomDatabase.getDatabase(context)
        popupDao = database!!.popupDao()
    }

    suspend fun insert(popup: Popup) = popupDao.insert(popup)

    suspend fun delete(popup: Popup) = popupDao.delete(popup)

    fun getPopup() = popupDao.getPopupSettings()

    // Function to get the most recent reward.
    fun getRecentPopup() = popupDao.getRecentPopupSetting()

    suspend fun deleteAll() = popupDao.deleteAll()
}