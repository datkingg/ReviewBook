package com.example.rebook.model

import java.io.Serializable

data class Users(val fullname:String, val email:String, val gender:String, val birthday: String, val password: String): Serializable {
}