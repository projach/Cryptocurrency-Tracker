package com.example.cryptocurrency_tracker.database

import androidx.room.Room
import androidx.room.Database
import android.content.Context
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 7, exportSchema = false)

abstract class DatabaseInstance : RoomDatabase(){
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getDatabase(context: Context): DatabaseInstance {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseInstance::class.java,
                    "coin_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}