package com.example.rebook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.databinding.ActivityPostBinding
import com.example.rebook.fragment.SubItemButtonClickListener
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Posts

class HomePostAdapter(
    private val context: Context,
    private var ps: List<Posts> = emptyList(),
    private val listener: SubItemButtonClickListener
): RecyclerView.Adapter<HomePostAdapter.PostHolder>() {
    private lateinit var helper: DatabaseHelper
    private lateinit var rs: Cursor
    private lateinit var binding: ActivityPostBinding
    inner class PostHolder(private val binding: ActivityPostBinding, private val listener: SubItemButtonClickListener) : RecyclerView.ViewHolder(binding.root){
        private var currentPost :Posts? = null
        fun bind(posts: Posts){
            currentPost = posts
            val bookId = currentPost!!.bookId
            helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val query = "select * from books where book_id in(select $bookId from posts)"
            rs = db.rawQuery(query,null)
            while (rs.moveToNext()){
                val imgByteArray = rs.getBlob(1)
                val name = rs.getString(2)
                val author = rs.getString(3)
                val category = rs.getInt(4)
                val NXB = rs.getString(5)
                binding.tvNXB.text = NXB
                binding.tvNameBook.text = name
                binding.tvTacGiaa.text = author
                binding.tvTheLoaij.text = category.toString()
                binding.tvTenSach.text = name
                binding.tvGioiThieu.setText(posts.body)
                val bitmapImg = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
                binding.imgBook.setImageBitmap(bitmapImg)
            }
            rs.close()
        }
        init {
            binding.imgSaveBook.setOnClickListener {
                currentPost?.let {
                    listener.onButtonClick(adapterPosition, 1, it)
                }

            }
            binding.imgBinhLuan.setOnClickListener {
                currentPost?.let {
                    listener.onButtonClick(adapterPosition,2, it)
                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ActivityPostBinding.inflate(inflater,parent,false)
        return PostHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = ps[position]
        return holder.bind(currentPost)
    }

    override fun getItemCount(): Int {
        return ps.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setPostList(posts: List<Posts>) {
        ps = posts
        notifyDataSetChanged()
    }

}