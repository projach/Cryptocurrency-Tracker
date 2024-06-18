package com.example.cryptocurrency_tracker.database

import androidx.room.Database

@Database(entities = [UserEntity::class], version = 1)
abstract class DatabaseInstance{
    abstract fun getUserDao(): UserDao
}