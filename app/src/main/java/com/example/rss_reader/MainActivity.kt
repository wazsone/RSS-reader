package com.example.rss_reader

import android.os.Bundle
import android.util.Log
import androidx.annotation.UiThread
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
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.simpleName.toString()

    private val url1 = "https://meduza.io/rss/podcasts/meduza-v-kurse"
    private val url2 = "https://habr.com/ru/rss/hubs/all/"

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
            val articles = mutableListOf<Article>()
            try {
                val parser = Parser()
                articles.addAll(parser.getArticles(url1))
                articles.addAll(parser.getArticles(url2))
                articles.sortByDescending {
                    val sourceDateString = it.pubDate.toString()
                    //Tue, 10 Sep 2019 12:21:23 +0300
                    val inputPattern = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                    try {
                        inputPattern.parse(sourceDateString)
                    } catch (e: java.lang.Exception) {
                        Log.d(TAG, "Couldn't parse pubData by the 'EEE, d MMM yyyy HH:mm:ss Z' format. StackTrace: ${e.stackTrace}")
                        //Fri, 27 Sep 2019 14:32:25 GMT
                        val inputPattern2 = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                        inputPattern2.parse(sourceDateString)
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Couldn't load news. StackTrace: ${e.stackTrace}")
            }
            adapter = ArticleAdapter(articles)
            main__rv_articles.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}
