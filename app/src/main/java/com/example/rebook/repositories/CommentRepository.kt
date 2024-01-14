package com.example.rebook.repositories

import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Comment

class CommentRepository(private val helper: DatabaseHelper) {

    fun insertComment(userId:Int, bookId:Int, status:Int, title:String):Long{
        return helper.insertComment(userId, bookId, status, title)
    }

    fun getAllData():List<Comment>{
        return helper.getAllComment()
    }
}