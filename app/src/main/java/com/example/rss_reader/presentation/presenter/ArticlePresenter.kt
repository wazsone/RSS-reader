package com.example.rss_reader.presentation.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.rss_reader.model.ArticleModel
import com.example.rss_reader.presentation.view.MainView
import com.prof.rssparser.Article

@InjectViewState
class ArticlePresenter : MvpPresenter<MainView>() {
    fun loadNews() {
        try {
            ArticleModel(this).fetchNews()
        } catch (e: Exception) {
            Log.d("TAG", "StackTrace: ")
            e.printStackTrace()
            viewState.showConnetionAlert()
        }
    }

    fun finishLoading(articles: MutableList<Article>) {
        viewState.setupArticlesList(articles)
    }
}