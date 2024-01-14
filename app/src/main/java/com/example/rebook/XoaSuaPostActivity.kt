package com.example.rebook

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.databinding.ActivityXoaSuaPostBinding
import com.example.rebook.factory.BookFactory
import com.example.rebook.factory.PostFactory
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookViewModel
import com.example.rebook.model.Books
import com.example.rebook.model.PostViewModel
import java.io.ByteArrayOutputStream

class XoaSuaPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityXoaSuaPostBinding
    private lateinit var helper: DatabaseHelper
    private lateinit var rs: Cursor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityXoaSuaPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = DatabaseHelper(this)
        val bundle = intent.extras
        if (bundle != null) {
            val bookId = bundle.getInt("bookId")
            val book = getBook(bookId)
            val categoryId = book.categoryId
            val categoryName = getCategory(categoryId)
            binding.tenBaiViet.text = book.name
            binding.edTacGia.setText(book.author)
            binding.edGioiThieu.setText(bundle.getString("body"))
            binding.edNhaXuatBan.setText(book.NXB)
            binding.edTheLoai.setText(categoryName)
            val imgByteArray = book.img
            val bitmapImg = BitmapFactory.decodeByteArray(imgByteArray, 0, imgByteArray.size)
            binding.imgSach.setImageBitmap(bitmapImg)
        }
        binding.btnLuuThayDoi.setOnClickListener {
            if (bundle != null) {
                val bookId = bundle.getInt("bookId")
                val adminID = bundle.getInt("adminId")
                val bookFactory = BookFactory(this)
                val book = getBook(bookId)

                helper.updateCategory(book.categoryId, binding.edTheLoai.text.toString())
                val bitmapDrawable: Drawable? = binding.imgSach.drawable
                val bitmap = (bitmapDrawable as BitmapDrawable).bitmap
                val byteArray = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
                val imgBook: ByteArray = byteArray.toByteArray()
                val viewModelBookViewModel =
                    ViewModelProvider(this, bookFactory)[BookViewModel::class.java]
                viewModelBookViewModel.updateBook(
                    bookId,
                    imgBook,
                    book.name,
                    binding.edTacGia.text.toString(),
                    book.categoryId,
                    binding.edNhaXuatBan.text.toString()
                )
                val selection = "book_id =?"
                val arr = arrayOf(bookId.toString())
                val db = helper.readableDatabase
                rs = db.rawQuery("select * from posts where $selection", arr)
                rs.moveToFirst()
                val postId = rs.getInt(0)
                val postBookFactory = PostFactory(this)
                val viewModelPost =
                    ViewModelProvider(this, postBookFactory)[PostViewModel::class.java]
                viewModelPost.updatePost(
                    postId,
                    adminID,
                    bookId,
                    binding.edGioiThieu.text.toString()
                )
                Toast.makeText(
                    this,
                    "Luu thay doi",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

        }

        binding.btnXoaBaiViet.setOnClickListener {
            if(bundle != null){
                val bookId = bundle.getInt("bookId")
                val db = helper.readableDatabase
                val selection = "book_id = ?"
                val arr = arrayOf(bookId.toString())
                rs = db.rawQuery("select * from posts where $selection", arr)
                rs.moveToFirst()
                db.delete("posts", "post_id =?", arrayOf(rs.getInt(0).toString()))
                Toast.makeText(
                    this,
                    "Xoa thanh cong",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
        binding.btnExit.setOnClickListener {
            val intent = Intent(
                this,
                BaiVietActivity::class.java
            )
            startActivity(intent)
        }

    }

    private fun getBook(
        bookId: Int
    ): Books {
        val db = helper.readableDatabase
        val selection = "book_id = ?"
        val selectionArgs = arrayOf(bookId.toString())
        rs = db.rawQuery("select * from books where $selection", selectionArgs)
        rs.moveToFirst()

        val bookName = rs.getString(2)
        val imgBook = rs.getBlob(1)
        val author = rs.getString(3)
        val categoryId = rs.getInt(4)
        val NXB = rs.getString(5)
        val book = Books(imgBook, bookName, author, categoryId, NXB)
        rs.close()
        return book
    }

    private fun getCategory(categoryId: Int): String {
        val db = helper.readableDatabase
        val selection = "category_id = ?"
        val arr = arrayOf(categoryId.toString())
        rs = db.rawQuery("select * from categories where $selection", arr)
        rs.moveToFirst()
        val category = rs.getString(1)
        rs.close()
        return category
    }
}