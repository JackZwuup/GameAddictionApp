package com.example.capstoneassignmentjack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "reward")
data class Reward (
    @ColumnInfo(name = "daterecent")
    var daterecent: Date,

    @ColumnInfo(name = "getstars")
    var getstars: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
)