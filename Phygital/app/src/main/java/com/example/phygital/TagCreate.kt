package com.example.phygital

data class LinkItem(
    val url: String,
    val description: String
)

data class ImageItem(
    val url: String,
    val description: String,
    val public_id: String
)

data class TagCreate(
    val author: String,
    val tag: String,
    val notes: String?,
    val links: List<LinkItem>?,
    val images: List<ImageItem>?
)