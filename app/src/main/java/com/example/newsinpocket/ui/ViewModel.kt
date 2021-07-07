package com.example.newsinpocket.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsinpocket.Resource
import com.example.newsinpocket.model.Article
import com.example.newsinpocket.model.NewsResponse
import com.example.newsinpocket.model.Source
import com.example.newsinpocket.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ViewModel(val repository: NewsRepository) : ViewModel() {


    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var newsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1

    var newsResponse: NewsResponse? = null
    var searchNewsResponse: NewsResponse? = null

    fun getNews(countryCode: String) = viewModelScope.launch{
        news.value = Resource.Loading()
        val response = repository.getAllNews(countryCode, newsPage)
        news.postValue(handleNewsResponse(response))
    }

    private fun handleNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                newsPage++
                if(newsResponse == null) {
                    newsResponse = resultResponse
                } else {
                    val oldArticles = newsResponse?.articles
                    val newsArticles = resultResponse.articles
                    oldArticles?.addAll(newsArticles)
                }
                return Resource.Success(newsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.searchForNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = newsResponse?.articles
                    val newsArticles = resultResponse.articles
                    oldArticles?.addAll(newsArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    init {
        getNews("ru")
    }

}