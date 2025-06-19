package com.example.mittise.ui.marketplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mittise.data.model.Product
import com.example.mittise.databinding.ItemProductBinding

class ProductViewHolder(
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        binding.apply {
            productName.text = product.name
            productPrice.text = "₹${product.price}"
            productLocation.text = product.location
            productSeller.text = "Seller: ${product.sellerName}"
            
            Glide.with(productImage)
                .load(product.imageUrl)
                .centerCrop()
                .into(productImage)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductViewHolder {
            val binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ProductViewHolder(binding)
        }
    }
} 