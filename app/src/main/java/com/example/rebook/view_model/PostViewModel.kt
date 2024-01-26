package com.example.rebook.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rebook.model.Posts
import com.example.rebook.repositories.PostRepository

class PostViewModel(private val postRepository: PostRepository):ViewModel() {

    private var _postList = MutableLiveData<List<Posts>>()
    val postList: LiveData<List<Posts>> get() = _postList

    fun updatePostList(newList: List<Posts>) {
        _postList.value = newList
    }
    fun insertPost(adminId:Int, bookId:Int, body:String):Long{
        return postRepository.insertPost(adminId, bookId, body)
    }
    fun updatePost(postId:Int, adminId:Int, bookId:Int, body:String):Int{
        return postRepository.updatePost(postId, adminId, bookId, body)
    }
}