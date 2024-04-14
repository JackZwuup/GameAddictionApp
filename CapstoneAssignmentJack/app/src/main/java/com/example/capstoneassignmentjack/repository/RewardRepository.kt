package com.example.capstoneassignmentjack.repository

import android.content.Context
import com.example.capstoneassignmentjack.model.Reward
import com.example.level5task2.database.RewardDao
import com.example.level5task2.database.RewardRoomDatabase

class RewardRepository(context: Context) {
    private val rewardDao: RewardDao

    init {
        val database = RewardRoomDatabase.getDatabase(context)
        rewardDao = database!!.rewardDao()
    }

    suspend fun insert(reward: Reward) = rewardDao.insert(reward)

    suspend fun delete(reward: Reward) = rewardDao.delete(reward)

    fun getRewards() = rewardDao.getRewards()
    suspend fun deleteAll() = rewardDao.deleteAll()
}