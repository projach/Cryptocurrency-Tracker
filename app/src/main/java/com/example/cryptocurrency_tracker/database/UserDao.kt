package com.example.cryptocurrency_tracker.database

import androidx.room.Dao
import kotlin.jvm.Throws
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Insert
import android.database.SQLException
import androidx.room.OnConflictStrategy

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(coin: UserEntity)

    @Query("UPDATE UserEntity SET current_price = :price WHERE name = :name")
    suspend fun updateData(price: Double, name:String)

    @Delete
    suspend fun delete(coin: UserEntity)

    @Query("DELETE FROM UserEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM UserEntity ORDER BY id ASC")
    suspend fun readAll() : List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE favourite = 1")
    suspend fun favourites(): List<UserEntity>
}