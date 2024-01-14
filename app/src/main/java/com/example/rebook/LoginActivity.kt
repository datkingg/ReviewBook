package com.example.rebook

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.databinding.ActivityLoginBinding
import com.example.rebook.databinding.ActivityRegisterBinding
import com.example.rebook.factory.BookFactory
import com.example.rebook.factory.PostFactory
import com.example.rebook.factory.UserViewModelFactory
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookViewModel
import com.example.rebook.model.PostViewModel
import com.example.rebook.model.UserViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var rs: Cursor
    private lateinit var binding: ActivityLoginBinding

//    private lateinit var viewModel: BookViewModel
    private lateinit var viewModelPost: PostViewModel
    private lateinit var viewModel: UserViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var helper = DatabaseHelper(applicationContext)
        db = helper.readableDatabase

//        val viewModelFactory = PostFactory(this)
//        val viewModelFactory2 = BookFactory(this)
//        viewModelPost = ViewModelProvider(this,viewModelFactory)[PostViewModel::class.java]
//        viewModelPost.insertPost(
//            1,
//            1,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        viewModelPost.insertPost(
//            1,
//            2,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        viewModelPost.insertPost(
//            1,
//            3,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        viewModelPost.insertPost(
//            1,
//            4,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        viewModelPost.insertPost(
//            1,
//            5,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        viewModelPost.insertPost(
//            1,
//            6,
//            "vjbnvjdsjfvhfvkjabsvygjdbfjhfgvuydbfvjdshbvdjhf"
//        )
//        val viewModelFactory = UserViewModelFactory(this@LoginActivity)
//        viewModel = ViewModelProvider(this,viewModelFactory)[UserViewModel::class.java]
//        viewModel.insertUser(
//            "duong nguyen",
//            "duong@gmail.com",
//            "nam",
//            "02/05/2002",
//            "12345678"
//        )
//        val viewModelFactory2 = BookFactory(this)
//        viewModel = ViewModelProvider(this, viewModelFactory2)[BookViewModel::class.java]
//        viewModel.insertBook(
//            "book1",
//            "Bạn tài giỏi, Tôi cũng thế",
//            "Adam",
//            1,
//            "Tom Book"
//        )
//        viewModel.insertBook(
//            "book2",
//            "Co gang het minh",
//            "Adam",
//            1,
//            "Tom Book"
//        )
//        viewModel.insertBook(
//            "book3",
//            "Mac ke thien ha",
//            "Adam",
//            1,
//            "Tom Book"
//        )
//        viewModel.insertBook(
//            "book4",
//            "Vi sao",
//            "Adam",
//            1,
//            "Tom Book"
//        )

        binding.btnLogin.setOnClickListener {
            if (TextUtils.isEmpty(binding.edEmail.text)) {
                binding.edEmail.error = "Email can not be empty"
                binding.edEmail.focusable
            } else if (TextUtils.isEmpty(binding.edPassword.text)) {
                binding.edPassword.error = "Password can not be empty"
                binding.edPassword.focusable
            } else {
                val query = isLoginUser(
                    binding.edEmail.text.toString(),
                    binding.edPassword.text.toString()
                )

                if (query == 'u') {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("email","${binding.edEmail.text}")
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
                else if (query == 'a'){
                    val intent = Intent(this@LoginActivity, HomeAdminActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("email","${binding.edEmail.text}")
                    intent.putExtras(bundle)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this@LoginActivity, "Email hoặc Password sai ", Toast.LENGTH_SHORT).show()
                    binding.edEmail.focusable
                    binding.edPassword.setText("")
                }

            }
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }


    }


    private fun isLoginUser(email: String, password: String): Char {

        val selection = "email = ? and password = ?"
        val selectionArg = arrayOf(email, password)
        rs = db.query("users", null, selection, selectionArg, null, null, null)
        if (rs.count > 0) {
            rs.moveToFirst()
            return if(rs.getInt(6) == 1){
                'a'
            } else{
                'u'
            }
        }
        return 'f'
    }
}