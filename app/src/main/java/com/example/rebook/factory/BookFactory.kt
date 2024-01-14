package com.example.rebook.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookViewModel
import com.example.rebook.repositories.BookRepository

class BookFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)){
            return BookViewModel(BookRepository(DatabaseHelper(context))) as T
        }
        throw IllegalArgumentException("Unknown")
    }
}