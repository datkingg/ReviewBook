package com.example.rebook.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rebook.repositories.UserRepository

class UserViewModel(private val repository: UserRepository): ViewModel() {

    fun updateUserData(idUser: Int, name: String, email: String, gender: String, birthday:String, password:String):Int{
        return repository.updateUserData(idUser,name,email,gender,birthday,password)
    }
    fun insertUser(name: String, email: String, gender: String, birthday:String, password:String):Long{
        return repository.insertUser(name, email, gender, birthday, password)
    }

    fun deleteUser(idUser:Int):Int{
        return repository.deleteUser(idUser)
    }
}