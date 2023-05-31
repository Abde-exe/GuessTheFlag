package com.example.guesstheflag

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userScoreDao(): UserScoreDao


}