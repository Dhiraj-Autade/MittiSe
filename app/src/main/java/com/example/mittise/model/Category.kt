package com.example.mittise.model

data class Category(
    val id: Int,
    val name: String,
    val iconResId: Int
) {
    val imageResId: Int = iconResId
}