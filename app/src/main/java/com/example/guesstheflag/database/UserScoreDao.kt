package com.example.guesstheflag.database

import androidx.room.*
import com.example.guesstheflag.UserScore

@Dao
interface UserScoreDao {
    @Query("SELECT * FROM user_score ORDER BY score DESC")
    fun getAllUserScores(): MutableList<UserScore>

    @Insert
    suspend fun insertUserScore(userScore: UserScore)

    @Delete
    suspend fun deleteUserScore(userScore: UserScore)
}

