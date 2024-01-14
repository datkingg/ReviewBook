package com.example.rebook.repositories

import com.example.rebook.helper.DatabaseHelper

class PostRepository(val helper: DatabaseHelper) {
    fun insertPost(adminId:Int, bookId:Int, body:String):Long{
        return helper.insertPost(adminId, bookId, body)
    }
    fun updatePost(postID:Int,adminId: Int, bookId: Int,body: String):Int{
        return helper.updatePost(postID,bookId, adminId, body)
    }
}