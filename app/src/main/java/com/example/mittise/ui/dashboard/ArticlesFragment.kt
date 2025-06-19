package com.example.mittise.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mittise.R
import androidx.recyclerview.widget.RecyclerView

class ArticlesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.articlesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ArticlesAdapter(getDemoArticles())
        return view
    }

    private fun getDemoArticles(): List<Article> = listOf(
        Article(
            title = "How to Increase Wheat Yield",
            summary = "Tips and tricks for maximizing your wheat production this season.",
            imageResId = R.drawable.ic_news
        ),
        Article(
            title = "Government Yojna: New Subsidies for Farmers",
            summary = "Learn about the latest government schemes and how to apply.",
            imageResId = R.drawable.ic_news
        ),
        Article(
            title = "Weather Alert: Preparing for Monsoon",
            summary = "Essential steps to protect your crops during heavy rains.",
            imageResId = R.drawable.ic_news
        )
    )
} 