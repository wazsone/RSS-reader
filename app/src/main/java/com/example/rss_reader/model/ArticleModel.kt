package com.example.rss_reader.model

import com.example.rss_reader.presentation.presenter.ArticlePresenter
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ArticleModel(private var presenter: ArticlePresenter) {

    private val url1 = "https://meduza.io/rss/podcasts/meduza-v-kurse"
    private val url2 = "https://habr.com/ru/rss/hubs/all/"

    private val job = Job()
    private val coroutinScope = CoroutineScope(Dispatchers.Main + job)

    fun fetchNews() {
        val articles = mutableListOf<Article>()
        coroutinScope.launch {
            val parser = Parser()
//            val deferreds = listOf(
//                async { articles.addAll(parser.getArticles(url1)) },
//                async { articles.addAll(parser.getArticles(url2)) }
//            )
//            deferreds.awaitAll()
            articles.addAll(parser.getArticles(url1))
            articles.addAll(parser.getArticles(url2))
            presenter.finishLoading(articles)
        }
    }

}