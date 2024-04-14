package com.example.level5task2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.capstoneassignmentjack.model.Reward
import kotlinx.coroutines.flow.Flow


@Dao
interface RewardDao {
    @Query("SELECT * from reward ORDER BY `daterecent` ASC")
    fun getRewards(): LiveData<List<Reward>>

    @Query("SELECT * FROM reward ORDER BY id DESC LIMIT 1")
    fun getRecentReward(): Flow<Reward?>
    @Insert
    suspend fun insert(reward: Reward)

    @Insert
    suspend fun insert(reward: List<Reward>)

    @Delete
    suspend fun delete(reward: Reward)

    @Query("DELETE from reward")
    suspend fun deleteAll()
}