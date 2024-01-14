package com.example.rebook.fragment

import com.example.rebook.model.Posts

interface SubItemButtonClickListener {
    fun onButtonClickUser(position: Int)
    fun onButtonClick(position: Int, buttonId:Int, posts: Posts)
    fun onButtonClickAdminPost(position: Int, buttonId: Int, posts: Posts)
}