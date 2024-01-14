package com.example.rebook.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rebook.repositories.CommentRepository

class CommentViewModel(private val repository: CommentRepository):ViewModel() {

    private val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>> get() = _commentList

    fun updateCommentList(newList: List<Comment>) {
        _commentList.value = newList
    }
    fun insertComment(userId:Int, bookId:Int, status:Int, title:String):Long{
        return repository.insertComment(userId, bookId, status, title)
    }



}