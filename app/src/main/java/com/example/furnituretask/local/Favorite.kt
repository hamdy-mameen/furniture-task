package com.example.furnituretask.local

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favorite(
    @PrimaryKey
    val product_id:Int,
    val image:String,
    val furniture:String,
    val description:String,
    val price:String
){


}
