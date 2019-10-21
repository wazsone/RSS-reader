package com.example.rss_reader.presentation.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.rss_reader.model.ArticleModel
import com.example.rss_reader.presentation.view.MainView
import kotlinx.coroutines.*

@InjectViewState
class ArticlePresenter : MvpPresenter<MainView>() {

    private val jobIO = Job()
    private val coroutinScopeIO = CoroutineScope(Dispatchers.IO + jobIO)

    fun loadNews() {
        try {
            coroutinScopeIO.launch {
                val articles = ArticleModel().fetchNews()
                withContext(Dispatchers.Main) {
                    if (articles == null) {
                        viewState.showConnetionAlert()
                    } else {
                        viewState.setupArticlesList(articles)
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("TAG", "StackTrace: ")
            e.printStackTrace()
            viewState.showConnetionAlert()
        }
    }

}
