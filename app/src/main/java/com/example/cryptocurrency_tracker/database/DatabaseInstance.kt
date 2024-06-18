package com.example.cryptocurrency_tracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class DatabaseInstance : RoomDatabase(){
    abstract fun getUserDao(): UserDao
}