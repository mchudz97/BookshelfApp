package com.example.bookshelfapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelfapp.BookshelfApplication
import com.example.bookshelfapp.data.BookRepository
import com.example.bookshelfapp.model.Book
import com.example.bookshelfapp.model.BookIdHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookshelfUiState{
    object Start : BookshelfUiState
    data class Success(val books: List<Book>) : BookshelfUiState
    data class NoContent(val phase: String) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val phase = mutableStateOf("")

    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init{
        bookshelfUiState = BookshelfUiState.Start
    }

    fun getBooks(){
        if(phase.value == ""){
            bookshelfUiState = BookshelfUiState.NoContent(phase.value)
            return
        }
        viewModelScope.launch{
            bookshelfUiState = BookshelfUiState.Loading

            val bookIds: List<BookIdHolder> =
                bookRepository.getBooksByPhase(phase.value).items
            bookshelfUiState = try {
                val books: List<Book> = bookIds.map {
                    async(Dispatchers.IO) {
                        bookRepository.getBook(it)
                    }.await()
                }
                if(books.isEmpty())
                    BookshelfUiState.NoContent(phase.value)
                else
                    BookshelfUiState.Success(books)

            } catch (e: IOException) {
                BookshelfUiState.Error
            } catch (e: HttpException) {
                BookshelfUiState.Error
            }
        }
    }

    fun updatePhase(newPhase: String){
        phase.value = newPhase
    }
    fun getPhase(): String{
        return phase.value
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookRepository = application.container.bookRepository
                BookshelfViewModel(bookRepository)
            }
        }
    }
}