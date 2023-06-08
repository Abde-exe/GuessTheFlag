package com.example.guesstheflag.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guesstheflag.UserScore

@Database(entities = [UserScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userScoreDao(): UserScoreDao


}