package com.example.newsinpocket.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsinpocket.MainActivity
import com.example.newsinpocket.R
import com.example.newsinpocket.Resource
import com.example.newsinpocket.adapters.NewsAdapter
import com.example.newsinpocket.db.NewsDatabase
import com.example.newsinpocket.repository.NewsRepository
import com.example.newsinpocket.util.Constants.Companion.QUERY_PAGE_SIZE
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.Job


class NewsFragment : Fragment(R.layout.fragment_news) {

    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application

        val newsRepository = NewsRepository(NewsDatabase(application))
        val viewModelProviderFactory = ViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ViewModel::class.java)


        val newsAdapter = NewsAdapter()
        news_recycler_view.adapter = newsAdapter
        news_recycler_view.addOnScrollListener(newsScrollListener)

        viewModel.news.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.articlesList = newsResponse.articles
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.newsPage == totalPages
                        if(isLastPage){
                            news_recycler_view.setPadding(0,0,0,100)
                        }
                        newsAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    showProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_newsFragment_to_articleFragment, bundle
            )
        }
        
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI
            .onNavDestinationSelected(
                item,
                requireView().findNavController()
            ) || super.onOptionsItemSelected(item)
    }

    private fun showProgressBar() {
        news_progress_bar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        news_progress_bar.visibility = View.INVISIBLE
        isLoading = false
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val newsScrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getNews("ru")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

}