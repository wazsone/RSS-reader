package com.example.rss_reader.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.prof.rssparser.Article

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun setupArticlesList(articles: MutableList<Article>)
    fun showConnetionAlert()
}