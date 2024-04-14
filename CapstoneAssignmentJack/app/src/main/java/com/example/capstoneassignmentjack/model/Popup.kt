package com.example.capstoneassignmentjack.model

import android.graphics.drawable.ColorDrawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popup")
data class Popup (
    @ColumnInfo(name = "text")
    var text: String,

    @ColumnInfo(name = "color")
    var color: String,

    @ColumnInfo(name = "size")
    var size: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
)