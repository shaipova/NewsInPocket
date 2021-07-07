package com.example.newsinpocket.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsinpocket.repository.NewsRepository

class ViewModelProviderFactory(val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModel(repository) as T
    }
}