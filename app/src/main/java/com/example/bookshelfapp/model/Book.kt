package com.example.bookshelfapp.model

import kotlinx.serialization.Serializable

@Serializable
data class BookIdHolder(val id: String)
@Serializable
data class AllItems(val items: List<BookIdHolder> = emptyList())
@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)
@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val imageLinks: ImageLinks? = null,
)
@Serializable
data class ImageLinks(
    val thumbnail: String? = null
)
