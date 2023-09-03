package com.example.bookshelfapp.data

import com.example.bookshelfapp.model.AllItems
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BookIdHolder
import com.example.bookshelfapp.network.BookshelfApiService

interface BookRepository {
    suspend fun getBook(bookIdHolder: BookIdHolder): Book
    suspend fun getBooksByPhase(phase: String): AllItems
}

class NetworkBookRepository(
    private val bookApiService: BookshelfApiService
) : BookRepository {
    override suspend fun getBook(bookIdHolder: BookIdHolder): Book {
        return bookApiService.getBook(bookIdHolder.id)
    }

    override suspend fun getBooksByPhase(phase: String): AllItems {
        return bookApiService.getBooksByPhase(phase)
    }

}