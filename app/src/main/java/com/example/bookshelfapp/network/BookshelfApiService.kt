package com.example.bookshelfapp.network

import com.example.bookshelfapp.model.AllItems
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BookIdHolder
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {
// https://www.googleapis.com/books/v1/volumes
    @GET("volumes")
    suspend fun getBooksByPhase(@Query("q") phase: String): AllItems
    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") bookId: String): Book

}