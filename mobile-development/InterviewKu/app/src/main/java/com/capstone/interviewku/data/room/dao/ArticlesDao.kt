package com.capstone.interviewku.data.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.interviewku.data.room.entity.ArticleEntity

@Dao
interface ArticlesDao {
    @Query("SELECT * FROM articles ORDER BY created_at DESC")
    fun getAllArticles(): PagingSource<Int, ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticles()
}
