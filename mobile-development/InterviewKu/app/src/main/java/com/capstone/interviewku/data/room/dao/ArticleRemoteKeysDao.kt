package com.capstone.interviewku.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.interviewku.data.room.entity.ArticleRemoteKey

@Dao
interface ArticleRemoteKeysDao {
    @Query("SELECT * FROM article_remote_keys WHERE id = :id")
    suspend fun getArticleRemoteKeyById(id: String): ArticleRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleRemoteKeys(remoteKeys: List<ArticleRemoteKey>)

    @Query("DELETE FROM article_remote_keys")
    suspend fun deleteAllArticleRemoteKeys()
}