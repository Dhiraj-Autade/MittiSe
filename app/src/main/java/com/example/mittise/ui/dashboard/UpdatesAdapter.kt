package com.example.mittise.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mittise.R
import com.example.mittise.model.Update

class UpdatesAdapter(
    private val updates: List<Update>,
    private val onUpdateClick: (Update) -> Unit
) : RecyclerView.Adapter<UpdatesAdapter.UpdateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_update, parent, false)
        return UpdateViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpdateViewHolder, position: Int) {
        holder.bind(updates[position])
    }

    override fun getItemCount() = updates.size

    inner class UpdateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.update_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.update_description)
        private val dateTextView: TextView = itemView.findViewById(R.id.update_date)
        private val imageView: ImageView = itemView.findViewById(R.id.update_image)

        init {
            itemView.setOnClickListener {
                onUpdateClick(updates[adapterPosition])
            }
        }

        fun bind(update: Update) {
            titleTextView.text = update.title
            descriptionTextView.text = update.description
            dateTextView.text = update.date
            imageView.setImageResource(update.imageResId)
        }
    }
} 