package com.example.rss_reader.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.rss_reader.R
import com.example.rss_reader.adapter.ArticleAdapter
import com.example.rss_reader.presentation.presenter.ArticlePresenter
import com.example.rss_reader.presentation.view.MainView
import com.prof.rssparser.Article
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {
    @InjectPresenter
    lateinit var articlePresenter: ArticlePresenter

    private val adapter: ArticleAdapter = ArticleAdapter(mutableListOf())

    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        init()
    }

    private fun init() {
        if (!isNetworkAvailable) {
            showConnetionAlert()
            Log.d("TAG", "StackTrace: MainActivity.init()")
        } else if (isNetworkAvailable) {
            articlePresenter.loadNews()
        }
    }

    private fun setupViews() {
        main__swipe_refresh_layout.setOnRefreshListener {
            articlePresenter.loadNews()
            main__swipe_refresh_layout.isRefreshing = false
        }

        main__rv_articles.layoutManager = LinearLayoutManager(this@MainActivity)
        main__rv_articles.itemAnimator = DefaultItemAnimator()
    }

    // View implementation
    override fun setupArticlesList(articles: MutableList<Article>) {
        adapter.setArticles(articles)
        adapter.sortArticlesByDate()
        main__rv_articles.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun showConnetionAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.alert_message)
            .setTitle(R.string.alert_title)
            .setCancelable(true)
            .setPositiveButton(R.string.alert_positive)
            { _, _ -> finish() }

        val alert = builder.create()
        alert.show()
    }
}
