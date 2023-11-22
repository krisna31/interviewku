package com.capstone.interviewku.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Article(
    @PrimaryKey
    val id: String
)
