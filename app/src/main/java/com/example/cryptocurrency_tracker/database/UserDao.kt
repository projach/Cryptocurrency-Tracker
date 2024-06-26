package com.example.cryptocurrency_tracker.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Insert
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

    @Query("SELECT * FROM UserEntity WHERE name LIKE '%' || :nameSymb || '%' OR symbol LIKE '%' || :nameSymb || '%'")
    fun search(nameSymb: String): List<UserEntity>

}