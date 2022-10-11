package com.example.kotlinmigration.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "posts_table")
data class PostDto(
    @ColumnInfo(name = "Body")
    val Body: String,
    @ColumnInfo(name = "ID")
    val ID: Int,
    @ColumnInfo(name = "UserID")
    val UserID: Int,
    @ColumnInfo(name = "Title")
    val Title: String

) {
}