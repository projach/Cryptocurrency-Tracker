package com.example.cryptocurrency_tracker.database

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @ColumnInfo(name = "id") @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "current_price") val currentPrice: String,
    @ColumnInfo(name = "favourite") val favourite: Boolean
)