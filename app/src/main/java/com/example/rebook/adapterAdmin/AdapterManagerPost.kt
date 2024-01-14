package com.example.rebook.adapterAdmin

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.databinding.ActivityPostBinding
import com.example.rebook.databinding.ItemPostAdminBinding
import com.example.rebook.fragment.SubItemButtonClickListener
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Posts

class AdapterManagerPost(
    private var ps:List<Posts> = emptyList(),
    private var context: Context,
    private val listener: SubItemButtonClickListener
    ):RecyclerView.Adapter<AdapterManagerPost.PostHolder>(){
    private lateinit var helper: DatabaseHelper
    private lateinit var rs: Cursor
    private lateinit var binding: ItemPostAdminBinding
    inner class PostHolder(binding: ItemPostAdminBinding, listener: SubItemButtonClickListener): RecyclerView.ViewHolder(binding.root){
        private var currentPost :Posts? = null
        fun bind(posts: Posts){
            currentPost = posts
            val bookId = currentPost!!.bookId
            helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val selection = "book_id = ?"
            val arr = arrayOf(bookId.toString())
            val query = "select * from books where $selection"
            rs = db.rawQuery(query,arr)
            while (rs.moveToNext()){
                val imgByteArray = rs.getBlob(1)
                val name = rs.getString(2)
                val author = rs.getString(3)
                val category = rs.getInt(4)
                val NXB = rs.getString(5)
                rs.close()

                val selectionCate = "category_id = ?"
                val arrCate = arrayOf(category.toString())
                rs = db.rawQuery("select * from categories where $selectionCate", arrCate)
                if(rs.moveToFirst()){
                    binding.tvNXB.text = NXB
                    binding.tvNameBook.text = name
                    binding.tvTacGiaa.text = author
                    binding.tvTheLoaij.text = rs.getString(1)
                    binding.tvTenSach.text = name
                    binding.tvGioiThieu.text = posts.body
                    val bitmapImg = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
                    binding.imgBook.setImageBitmap(bitmapImg)
                    rs.close()
                }
            }

        }

        init {
            binding.btnChangePost.setOnClickListener {
                currentPost?.let {
                    listener.onButtonClickAdminPost(adapterPosition,1, it)
                }
            }
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun setPostList(posts: List<Posts>) {
        ps = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemPostAdminBinding.inflate(inflater,parent,false)
        return PostHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return ps.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = ps[position]
        return holder.bind(currentPost)
    }

}