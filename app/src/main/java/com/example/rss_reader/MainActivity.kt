package com.example.rss_reader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val url = "https://meduza.io/rss/podcasts/meduza-v-kurse"
    private val job = Job()
    private val coroutinScope = CoroutineScope(Dispatchers.Main + job)
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main__rv_articles.layoutManager = LinearLayoutManager(this@MainActivity)
        main__rv_articles.itemAnimator = DefaultItemAnimator()
        fetchFeed()

        main__swipe_refresh_layout.setOnRefreshListener {
            fetchFeed()
            main__swipe_refresh_layout.isRefreshing = false
        }
    }

    private fun fetchFeed() {
        coroutinScope.launch {
            val articles = try {
                val parser = Parser()
                parser.getArticles(url)
            } catch (e: Exception) {
                mutableListOf<Article>()
            }
            adapter = ArticleAdapter(articles)
            main__rv_articles.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}
