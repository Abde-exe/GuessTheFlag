package com.example.guesstheflag

import androidx.room.*

@Dao
interface UserScoreDao {
    @Query("SELECT * FROM user_score ORDER BY score DESC")
    fun getAllUserScores(): MutableList<UserScore>

    @Insert
    suspend fun insertUserScore(userScore: UserScore)

    @Delete
    suspend fun deleteUserScore(userScore: UserScore)
}

