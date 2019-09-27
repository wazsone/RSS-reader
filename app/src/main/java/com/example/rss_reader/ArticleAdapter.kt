package com.example.rss_reader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.prof.rssparser.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_item.view.*

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
        fun bind(article: Article) {
            itemView.article_item__date.text = article.pubDate
            itemView.article_item__title.text = article.title
            Picasso.get().load(article.image)
                .placeholder(R.drawable.placeholder)
                .into(itemView.article_item__image)

            itemView.article_item__description.text = article.description
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