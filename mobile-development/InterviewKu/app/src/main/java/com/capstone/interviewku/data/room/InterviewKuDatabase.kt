package com.capstone.interviewku.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstone.interviewku.data.room.dao.ArticleRemoteKeysDao
import com.capstone.interviewku.data.room.dao.ArticlesDao
import com.capstone.interviewku.data.room.entity.ArticleEntity
import com.capstone.interviewku.data.room.entity.ArticleRemoteKey

@Database(
    version = 1,
    entities = [ArticleEntity::class, ArticleRemoteKey::class],
    exportSchema = false
)
abstract class InterviewKuDatabase : RoomDatabase() {
    abstract fun getArticlesDao(): ArticlesDao
    abstract fun getArticleRemoteKeysDao(): ArticleRemoteKeysDao
}