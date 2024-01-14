package com.example.rebook.model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.rebook.repositories.BookRepository

class BookViewModel(private val bookRepository: BookRepository): ViewModel() {
    fun insertBook(img: ByteArray, name:String, author:String, categoryId:Int, NXB:String): Long {
        return bookRepository.insertBook(img, name, author, categoryId, NXB)
    }
    fun updateBook(bookId:Int, img: ByteArray, name:String, author:String, categoryId:Int, NXB:String):Int{
        return bookRepository.updateBook(bookId, img, name, author, categoryId, NXB)
    }
}