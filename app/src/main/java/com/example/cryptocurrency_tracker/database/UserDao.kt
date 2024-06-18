package com.example.cryptocurrency_tracker.database

import android.database.SQLException
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlin.jvm.Throws

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(data:UserEntity)

    @Delete
    fun delete(data: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun readAll() : List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE favourite = 'true'")
    fun readFavourites() : List<UserEntity>
}