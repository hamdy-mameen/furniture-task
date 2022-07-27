package com.example.furnituretask.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteDatabase:RoomDatabase() {
    abstract fun  getFavoriteDao():FavoriteDao
}