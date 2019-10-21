package com.example.rss_reader.model

import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class ArticleModel() {

    private val url1 = "https://meduza.io/rss/podcasts/meduza-v-kurse"
    private val url2 = "https://habr.com/ru/rss/hubs/all/"

    suspend fun fetchNews(): MutableList<Article>? {
        if (!isOnline()) {
            return null
        }
        val articles = mutableListOf<Article>()
        val parser = Parser()
        articles.addAll(parser.getArticles(url1))
        articles.addAll(parser.getArticles(url2))
        return articles
    }

    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }

    }

}