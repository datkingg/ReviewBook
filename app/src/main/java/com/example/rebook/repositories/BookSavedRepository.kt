package com.example.rebook.repositories

import com.example.rebook.helper.DatabaseHelper

class BookSavedRepository(private val helper: DatabaseHelper) {
    fun insertBook(bookId: Int, userId: Int):Long{
        return helper.insertSaveBook(bookId, userId)
    }
}