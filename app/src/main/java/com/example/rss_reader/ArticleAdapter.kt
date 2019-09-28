package com.example.rss_reader

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_item.view.*
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private val articles: MutableList<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.article_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) =
        holder.bind(articles[position])

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = ArticleViewHolder::class.simpleName.toString()
        fun bind(article: Article) {
            itemView.article_item__date.text = try {
                val sourceDateString = article.pubDate.toString()
                //Tue, 10 Sep 2019 12:21:23 +0300
                val inputPattern = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH)
                val date = try {
                    inputPattern.parse(sourceDateString)
                } catch (e: java.lang.Exception) {
                    Log.d(
                        TAG,
                        "Couldn't parse pubData by the 'EEE, d MMM yyyy HH:mm:ss z' format. StackTrace: ${e.stackTrace}"
                    )
                    //Fri, 27 Sep 2019 14:32:25 GMT
                    val inputPattern2 =
                        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                    inputPattern2.parse(sourceDateString)
                }
                val outputPattern = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                outputPattern.format(date)
            } catch (e: ParseException) {
                Log.d(TAG, "Couldn't parse pubData. StackTrace: ${e.stackTrace}")
                article.pubDate
            }
            itemView.article_item__title.text = article.title
            Picasso.get().load(article.image)
                .placeholder(R.drawable.placeholder)
                .into(itemView.article_item__image)
            itemView.article_item__description.text =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(article.description, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(article.description)
                }
            itemView.article_item__source.text =
                itemView.context.getString(R.string.article_url, URL(article.link).host)
            itemView.setOnClickListener {
                if (itemView.article_item__description.visibility == View.GONE) {
                    itemView.article_item__description.visibility = View.VISIBLE
                } else {
                    itemView.article_item__description.visibility = View.GONE
                }
            }
        }
    }
}