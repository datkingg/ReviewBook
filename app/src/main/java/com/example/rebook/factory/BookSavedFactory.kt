package com.example.rebook.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookSaved
import com.example.rebook.model.BookSavedViewModel
import com.example.rebook.repositories.BookSavedRepository

class BookSavedFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookSavedViewModel::class.java)){
            return BookSavedViewModel(BookSavedRepository(DatabaseHelper(context))) as T
        }
        throw IllegalArgumentException("Unknown")
    }
}