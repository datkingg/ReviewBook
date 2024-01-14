package com.example.rebook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.databinding.BookItemRecyclerviewBinding
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookSaved
import com.example.rebook.model.Posts

class SavedBookAdapter(
    private val context: Context,
    private var ds: List<BookSaved> = emptyList()
): RecyclerView.Adapter<SavedBookAdapter.SavedBookHolder>() {
    private lateinit var helper: DatabaseHelper
    private lateinit var rs: Cursor
    inner class SavedBookHolder(private val binding: BookItemRecyclerviewBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(savedBook: BookSaved) {
            val bookId = savedBook.bookId

            helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val query = "select * from books where book_id in(select $bookId from book_saved)"
            rs = db.rawQuery(query, null)
            while (rs.moveToNext()) {
                val img = rs.getBlob(1)
                val name = rs.getString(2)
                val author = rs.getString(3)
                val bitmap = BitmapFactory.decodeByteArray(img,0,img.size)
                binding.imgBook.setImageBitmap(bitmap)
                binding.txtBookName.text = name
                binding.txtAuthor.text = author
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedBookAdapter.SavedBookHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookItemRecyclerviewBinding.inflate(inflater,parent,false)
        return SavedBookHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedBookAdapter.SavedBookHolder, position: Int) {
        val current = ds[position]
        return holder.bind(current)
    }

    override fun getItemCount(): Int {
        return ds.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setBookList(savedBook: List<BookSaved>) {
        ds = savedBook
        notifyDataSetChanged()
    }
}