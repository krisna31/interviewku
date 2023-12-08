package com.capstone.interviewku.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "subtitle")
    val subtitle: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "coverImgUrl")
    val coverImgUrl: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String?,
)
