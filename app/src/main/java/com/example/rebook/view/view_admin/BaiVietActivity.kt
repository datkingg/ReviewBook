package com.example.rebook.view.view_admin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rebook.adapterAdmin.AdapterManagerPost
import com.example.rebook.databinding.ActivityBaiVietBinding
import com.example.rebook.factory.PostFactory
import com.example.rebook.fragment.SubItemButtonClickListener
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.view_model.PostViewModel
import com.example.rebook.model.Posts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class BaiVietActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaiVietBinding
    private lateinit var helper: DatabaseHelper
    private lateinit var adapter: AdapterManagerPost
    private lateinit var viewModel: PostViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaiVietBinding.inflate(layoutInflater)
        setContentView(binding.root)
        helper = DatabaseHelper(this)

        val factory = PostFactory(this)
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]

        val bundle = intent.extras
        binding.btnAddPost.setOnClickListener {
            if (bundle != null) {
                val intent = Intent(this@BaiVietActivity, BaiVietMoiActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        binding.btnExit.setOnClickListener {
            onBackPressed()
        }

        val subItemButtonClickListener = object : SubItemButtonClickListener {
            override fun onButtonClickUser(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onButtonClick(position: Int, buttonId: Int, posts: Posts) {
            }

            override fun onButtonClickAdminPost(position: Int, buttonId: Int, posts: Posts) {
                when (buttonId) {
                    1 -> {
                        val intent = Intent(this@BaiVietActivity, XoaSuaPostActivity::class.java)
                        var bookId = posts.bookId
                        var adminId = posts.adminId
                        val bundle = Bundle()
                        bundle.putInt("bookId", bookId)
                        bundle.putInt("adminId", adminId)
                        bundle.putString("body", posts.body)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }
        }
        loadDataFromDatabase()
        binding.rvPostAdmin.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        viewModel.postList.observe(
            this
        ) { newList ->
            adapter = AdapterManagerPost(newList, this, subItemButtonClickListener)
            binding.rvPostAdmin.adapter = adapter
        }
    }

    private fun loadDataFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val postList = helper.getAllPost()
            withContext(Dispatchers.Main) {
                viewModel.updatePostList(postList)
            }
        }
    }

    override fun onRestart() {
        loadDataFromDatabase()
        super.onRestart()
    }
}