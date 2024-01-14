package com.example.rebook.factory

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.MainActivity
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.UserViewModel
import com.example.rebook.repositories.UserRepository

class UserViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(UserRepository(DatabaseHelper(context))) as T
        }
        throw IllegalArgumentException("Unknown")
    }

}