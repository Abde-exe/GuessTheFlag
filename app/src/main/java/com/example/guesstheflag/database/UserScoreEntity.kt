package com.example.guesstheflag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_score")
data class UserScore(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo (name = "name") val name: String,
    @ColumnInfo (name = "score") val score: Int
)
