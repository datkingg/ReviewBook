package com.example.rebook.model

import androidx.lifecycle.ViewModel
import com.example.rebook.repositories.BookSavedRepository

class BookSavedViewModel(private val repository: BookSavedRepository): ViewModel() {
    fun insertBookSaved(bookId: Int, userId:Int):Long{
        return repository.insertBook(bookId, userId)
    }
}