package com.example.rebook.repositories

import android.graphics.Bitmap
import com.example.rebook.helper.DatabaseHelper

class BookRepository(private val helper: DatabaseHelper) {
    fun insertBook(img: ByteArray, name:String, author: String, categoryId: Int, NXB: String):Long{
        return helper.insertBooks(img,name, author, categoryId, NXB)
    }
    fun updateBook(bookId:Int, img: ByteArray, name:String, author: String, categoryId: Int, NXB: String):Int{
        return helper.updateBook(bookId,img,name,categoryId, author, NXB)
    }
}