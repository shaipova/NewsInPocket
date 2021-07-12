package com.example.newsinpocket.repository

import com.example.newsinpocket.api.RetrofitInstance
import com.example.newsinpocket.db.NewsDatabase
import com.example.newsinpocket.model.Article

class NewsRepository(val db: NewsDatabase) {

    suspend fun getAllNews(countryCode: String, pageNumber: Int) = RetrofitInstance.api.getNews(countryCode, pageNumber)
    suspend fun searchForNews(searchQuery: String, pageNumber: Int) = RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    // db
//    suspend fun insert(article: Article) = db.getArticleDao().insert(article)
//    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
//    fun getAllArticles() = db.getArticleDao().getAllArticles()
}