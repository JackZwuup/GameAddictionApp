package com.example.capstoneassignmentjack.popupdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstoneassignmentjack.model.Popup
import com.example.capstoneassignmentjack.model.Reward
import kotlinx.coroutines.flow.Flow


@Dao
interface PopupDao {
    @Query("SELECT * from popup")
    fun getPopupSettings(): LiveData<List<Popup>>

    @Query("SELECT * from popup ORDER BY id DESC LIMIT 1")
    fun getRecentPopupSetting(): LiveData<Popup?>
    @Insert
    suspend fun insert(popup: Popup)

    @Insert
    suspend fun insert(popup: List<Popup>)

    @Delete
    suspend fun delete(popup: Popup)

    @Query("DELETE from popup")
    suspend fun deleteAll()
}