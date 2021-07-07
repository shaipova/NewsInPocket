package com.example.newsinpocket.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsinpocket.model.Article

@Dao
interface DAO {
    @Delete
    suspend fun deleteArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>
}