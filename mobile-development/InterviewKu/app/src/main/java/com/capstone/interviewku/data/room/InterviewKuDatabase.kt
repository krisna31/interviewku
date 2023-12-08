package com.capstone.interviewku.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.interviewku.data.room.dao.ArticlesDao
import com.capstone.interviewku.data.room.entity.ArticleEntity

@Database(
    version = 1,
    entities = [ArticleEntity::class],
    exportSchema = false
)
abstract class InterviewKuDatabase : RoomDatabase() {
    abstract fun getArticlesDao(): ArticlesDao
}