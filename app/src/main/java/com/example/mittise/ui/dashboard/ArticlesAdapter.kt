package com.example.mittise.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mittise.R

class ArticlesAdapter(private val articles: List<Article>) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.size

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.articleTitle)
        private val summary: TextView = itemView.findViewById(R.id.articleSummary)
        private val image: ImageView = itemView.findViewById(R.id.articleImage)

        fun bind(article: Article) {
            title.text = article.title
            summary.text = article.summary
            image.setImageResource(article.imageResId)
        }
    }
}

data class Article(val title: String, val summary: String, val imageResId: Int) 