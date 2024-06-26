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
    fun save(coin: UserEntity)

    @Query("UPDATE UserEntity SET current_price = :price WHERE name = :name")
    fun updateData(price: Double, name:String)

    @Delete
    fun delete(coin: UserEntity)

    @Query("DELETE FROM UserEntity")
    fun deleteAll()

    @Query("SELECT * FROM UserEntity ORDER BY id ASC")
    fun readAll() : List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE favourite = 1")
    fun favourites(): List<UserEntity>
}