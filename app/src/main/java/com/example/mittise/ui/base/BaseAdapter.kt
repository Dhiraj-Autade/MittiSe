package com.example.mittise.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T : Any, VB : ViewBinding, VH : RecyclerView.ViewHolder>(
    private val diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {

    abstract fun createViewHolder(binding: VB): VH
    abstract fun getViewBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = getViewBinding(LayoutInflater.from(parent.context), parent)
        return createViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        bindViewHolder(holder, item)
    }

    abstract fun bindViewHolder(holder: VH, item: T)
}

// Extension function to create a simple DiffUtil.ItemCallback
fun <T : Any> createDiffCallback(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
} 