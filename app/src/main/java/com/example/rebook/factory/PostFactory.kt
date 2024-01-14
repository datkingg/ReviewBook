package com.example.rebook.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.PostViewModel
import com.example.rebook.repositories.PostRepository

class PostFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)){
            return PostViewModel(PostRepository(DatabaseHelper(context))) as T
        }
        throw IllegalArgumentException("Unknown")
    }
}