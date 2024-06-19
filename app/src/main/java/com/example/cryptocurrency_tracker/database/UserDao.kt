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

    @Query("UPDATE UserEntity SET current_price = :price WHERE id = :id")
    fun updateData(price: Double, id:Int)

    @Delete
    fun delete(data: UserEntity)

    @Query("DELETE FROM UserEntity")
    fun deleteAll()

    @Query("SELECT * FROM UserEntity ORDER BY id ASC")
    fun readAll() : List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE favourite = 1")
    fun favourites() : List<UserEntity>
}