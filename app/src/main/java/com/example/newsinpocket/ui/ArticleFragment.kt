package com.example.newsinpocket.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsinpocket.MainActivity
import com.example.newsinpocket.R
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment(R.layout.fragment_article) {

    //lateinit var viewModel: ViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //получить переданные из другого фрагмента данные
        val article = args.article

        //подключить данные к отображению в fragment_article в webView
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

    }

}