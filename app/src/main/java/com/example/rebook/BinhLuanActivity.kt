package com.example.rebook

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rebook.adapter.CommentAdapter
import com.example.rebook.databinding.ActivityBinhLuanBinding
import com.example.rebook.factory.CommentFactory
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.CommentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class BinhLuanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBinhLuanBinding
    private lateinit var helper: DatabaseHelper
    private lateinit var adapter: CommentAdapter
    private lateinit var viewModel: CommentViewModel
    private var userId: Int = 0
    private var bookId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged", "Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBinhLuanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        helper = DatabaseHelper(this)
        val factory = CommentFactory(this)
        viewModel = ViewModelProvider(this, factory)[CommentViewModel::class.java]

        binding.btnThoatBinhLuan.setOnClickListener { onBackPressed() }

        val bundle = intent.extras
        if (bundle != null) {
            userId = bundle.getInt("userId")
            bookId = bundle.getInt("bookId")
        }

        var check = 0

        val db = helper.readableDatabase
        val rs = db.rawQuery("select * from comments_book", null)
        if (rs.moveToFirst()) {
            do {
                if (bookId == rs.getInt(2)) {
                    check++
                }
            } while (rs.moveToNext())
        }

        if (check != 0) {
            loadDataFromDatabase()
            binding.rvComment.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )

            viewModel.commentList.observe(this) { newList ->
                adapter = CommentAdapter(this, newList)
                binding.rvComment.adapter = adapter
            }

        } else {
            binding.rvComment.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
            viewModel.commentList.observe(this) { newList ->
                adapter = CommentAdapter(this, newList)
                binding.rvComment.adapter = adapter
            }
        }

        binding.btnCMT.setOnClickListener {
            var rate = binding.rateUser.rating.toInt()
            val title = binding.edBinhLuan.text.toString().trim()
            if (TextUtils.isEmpty(rate.toString())) {
                rate = 5
            } else if (TextUtils.isEmpty(title)) {
                binding.edBinhLuan.error = "Comment is empty"
                binding.edBinhLuan.focusable
            } else {
//                Toast.makeText(this, "$rate", Toast.LENGTH_SHORT).show()
                viewModel.insertComment(userId, bookId, rate, title)
                loadDataFromDatabase()
                binding.rateUser.rating = 0.0f
                binding.edBinhLuan.setText("")
            }

        }

    }

    private fun loadDataFromDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val commentList = helper.getCommentFilter(bookId)
            withContext(Dispatchers.Main) {
                viewModel.updateCommentList(commentList)
            }
        }
    }

}