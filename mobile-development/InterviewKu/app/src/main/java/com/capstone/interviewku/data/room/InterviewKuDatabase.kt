package com.capstone.interviewku.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.interviewku.data.room.dao.ArticlesDao
import com.capstone.interviewku.data.room.entity.Article

@Database(
    version = 1,
    entities = [Article::class]
)
abstract class InterviewKuDatabase : RoomDatabase() {
    abstract fun getArticlesDao(): ArticlesDao

    companion object {
        private const val DATABASE_NAME = "db_interviewku"

        private var instance: InterviewKuDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    InterviewKuDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        instance = it
                    }
            }
    }
}