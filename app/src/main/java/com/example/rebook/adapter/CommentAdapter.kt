package com.example.rebook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.databinding.ActivityBinhLuanBinding
import com.example.rebook.databinding.ItemBinhLuanBinding
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Comment

class CommentAdapter(private val context: Context, private var ds: List<Comment> = emptyList()) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    private lateinit var helper: DatabaseHelper
    private lateinit var binding: ItemBinhLuanBinding

    inner class CommentHolder(private val binding: ItemBinhLuanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentComment: Comment? = null
        fun bind(comment: Comment) {
            currentComment = comment
            helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val selection = "user_id = ?"
            val arr = arrayOf(comment.userId.toString())
            val query = "select * from users where $selection"
            val rs = db.rawQuery(query, arr)
            if (rs.moveToFirst()) {
                binding.tvTenUser.text = rs.getString(1)
                binding.tvNoiDungBinhLuan.text = currentComment!!.title
                binding.rating.rating = currentComment!!.status.toFloat()
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBinhLuanBinding.inflate(inflater, parent, false)
        return CommentHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentHolder, position: Int) {
        return holder.bind(ds[position])
    }

    override fun getItemCount(): Int {
        return ds.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListComment(comments: List<Comment>) {
        ds = comments
        notifyDataSetChanged()
    }
}