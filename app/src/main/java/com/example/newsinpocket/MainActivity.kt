package com.example.newsinpocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.newsinpocket.db.NewsDatabase
import com.example.newsinpocket.repository.NewsRepository
import com.example.newsinpocket.ui.ViewModel
import com.example.newsinpocket.ui.ViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    //lateinit var viewModel : ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val newsRepository = NewsRepository(NewsDatabase(this))
//        val viewModelProviderFactory = ViewModelProviderFactory(newsRepository)
//        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ViewModel::class.java)
    }
}