package com.example.rebook.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookViewModel
import com.example.rebook.model.CommentViewModel
import com.example.rebook.repositories.BookRepository
import com.example.rebook.repositories.CommentRepository

class CommentFactory(private val context: Context):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CommentViewModel::class.java)){
            return CommentViewModel(CommentRepository(DatabaseHelper(context))) as T
        }
        throw IllegalArgumentException("Unknown")
    }
}