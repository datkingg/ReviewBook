package com.example.rebook.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.CaseMap.Title
import androidx.core.content.contentValuesOf
import com.example.rebook.model.BookSaved
import com.example.rebook.model.Comment
import com.example.rebook.model.Posts
import com.example.rebook.model.Users
import java.io.ByteArrayOutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "DB_RE_BOOK", null, 1){
    lateinit var db: SQLiteDatabase
    private lateinit var rs:Cursor
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table if not exists users (user_id integer primary key autoincrement , full_name varchar(255), email varchar(255), gender varchar(5), birthday varchar(255), password varchar(255), status varchar(10))")
        db?.execSQL("create table if not exists books (book_id integer primary key autoincrement , img_book blob , book_name varchar(255), author varchar(255), category_id integer, NXB varchar(255), foreign key (category_id) references categories(category_id))")
        db?.execSQL("create table if not exists categories (category_id integer primary key autoincrement , category_name varchar(255))")
        db?.execSQL("create table if not exists posts (post_id integer primary key autoincrement , admin_id integer,  book_id integer, body text, foreign key (admin_id) references admins(admin_id), foreign key (book_id) references books(book_id))")
        db?.execSQL("create table if not exists admins (admin_id integer primary key autoincrement, name varchar(255), username varchar(255), SDT varchar(255), email varchar(255), status varchar(10))")
        db?.execSQL("create table if not exists comments_book (comment_id integer primary key autoincrement, user_id integer, book_id integer, status integer, title text,foreign key (user_id) references users(user_id), foreign key (book_id) references books(book_id))")
        db?.execSQL("create table if not exists book_saved (id integer primary key autoincrement, book_id integer, user_id integer, foreign key (book_id) references books(book_id), foreign key (user_id) references users(user_id))")
        db?.execSQL("insert into categories ('category_name') values ('sach hay')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table exist" + "users")
        onCreate(db)
        db?.execSQL("drop table exist" + "books")
        onCreate(db)
        db?.execSQL("drop table exist" + "categories")
        onCreate(db)
    }

    fun insertSaveBook(bookId:Int, userId:Int): Long{
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("book_id", bookId)
            put("user_id", userId)
        }
        return db.insert("book_saved",null, cv)
    }

    fun getAllDataBookSaved(userId: Int):List<BookSaved>{
        val bs = mutableListOf<BookSaved>()
        val db = this.readableDatabase
        val selection = "user_id = ?"
        val selectionArg = arrayOf(userId.toString())

        rs = db.query("book_saved",null,selection,selectionArg,null,null,null)
        if(rs.moveToFirst()){
            do {
                val bookSaved = BookSaved(rs.getInt(1), rs.getInt(2))
                bs.add(bookSaved)
            }while (rs.moveToNext())
        }
         return bs
    }

    fun insertBooks(img:ByteArray, name:String, author:String, categoryId: Int, NXB: String): Long {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("img_book",img)
            put("book_name", name)
            put("author", author)
            put("category_id", categoryId)
            put("NXB", NXB)
        }
        return db.insert("books",null, cv)
    }


    fun updateUserData(idUser: Int, full_name:String,email:String, gender:String, birthday:String, password:String ):Int{

        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("full_name", full_name)
            put("email", email)
            put("gender", gender)
            put("birthday", birthday)
            put("password", password)
        }
        return db.update("users",cv,"user_id=?", arrayOf(idUser.toString()))
    }

    fun insertUser(full_name:String,email:String, gender:String, birthday:String, password:String ): Long{
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("full_name", full_name)
            put("email", email)
            put("gender", gender)
            put("birthday", birthday)
            put("password", password)
            put("status", 1)
        }
        return db.insert("users",null, cv)
    }

    fun deleteUser(idUser: Int):Int{
        val db = this.writableDatabase
        return db.delete("users","user_id = ?", arrayOf(idUser.toString()))
    }

    fun insertPost(adminId:Int, bookId:Int, body:String): Long{
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("admin_id",adminId)
            put("book_id", bookId)
            put("body", body)
        }
        return db.insert("posts",null,cv)
    }

    fun getAllPost(): List<Posts>{
        db = this.readableDatabase
        val postList = mutableListOf<Posts>()
        val rs = db.rawQuery("select * from posts",null)

        if (rs.moveToFirst()){
            do {
                val adminId = rs.getInt(1)
                val bookId = rs.getInt(2)
                val body = rs.getString(3)
                val post = Posts(adminId, bookId, body)
                postList.add(post)
            }while (rs.moveToNext())
        }
        rs.close()
        return postList
    }

    fun getAllUser():List<Users>{
        db = this.readableDatabase
        val userList = mutableListOf<Users>()
        val rs = db.rawQuery("select * from users",null)
        if(rs.moveToFirst()){
            do {
                val name = rs.getString(1)
                val email = rs.getString(2)
                val gender = rs.getString(3)
                val birthday = rs.getString(4)
                val password = rs.getString(5)
                val user = Users(name,email,gender, birthday, password)
                userList.add(user)
            }while (rs.moveToNext())
        }
        rs.close()
        return userList
    }

    fun insertCategory(category: String):Long{
        db = this.readableDatabase
        val rs = db.rawQuery("select * from categories where category_name like '$category'",null)
        if(rs.moveToFirst()){
            return 1
        }

        db = this.writableDatabase
        val cv = ContentValues().apply {
            put("category_name",category)
        }
        return db.insert("categories",null, cv)
    }

    fun updateBook(bookId:Int, imgBook: ByteArray, bookName: String, categoryId: Int,author: String,NXB: String):Int{
        db = this.writableDatabase
        val selection = "book_id = ?"
        val arr = arrayOf(bookId.toString())
        val rs = db.rawQuery("select * from books where $selection", arr)
        rs.moveToFirst()
        val cv = ContentValues().apply {
            put("img_book", imgBook)
            put("book_name", bookName)
            put("author", author)
            put("category_id", categoryId)
            put("NXB", NXB)
        }
        return db.update("books", cv, "book_id =?", arrayOf(rs.getInt(0).toString()))
    }
    fun updatePost(postId:Int, bookId: Int, adminId: Int, body: String):Int{
        db = this.writableDatabase
        val selection = "post_id = ?"
        val arr = arrayOf(postId.toString())
        val rs = db.rawQuery("select * from posts where $selection", arr)
        rs.moveToFirst()
        val cv = ContentValues().apply {
            put("admin_id", adminId)
            put("book_id", bookId)
            put("body", body)
        }
        return db.update("posts",cv, "post_id =?" , arrayOf(rs.getInt(0).toString()))
    }
    fun updateCategory(categoryId:Int, category: String):Int{
        db = this.writableDatabase
        val selection = "category_id = ?"
        val arr = arrayOf(categoryId.toString())
        val rs = db.rawQuery("select * from categories where $selection", arr)
        rs.moveToFirst()

        if(category.contains(rs.getString(1),true)){
            return 0
        }
        else{
            val cv = ContentValues().apply {
                put("category_name", category)
            }
            return db.update("categories", cv, "category_id =?", arrayOf(rs.getInt(0).toString()))
        }
    }

    fun insertComment(userId: Int, bookId: Int, status:Int, title: String):Long{
        db = this.writableDatabase
        val cv = ContentValues().apply {
            put("user_id", userId)
            put("book_id", bookId)
            put("status", status)
            put("title", title)
        }
        return db.insert("comments_book", null, cv)
    }

    fun getAllComment(): List<Comment>{
        db = this.readableDatabase
        var list = mutableListOf<Comment>()
        val rs = db.rawQuery("select * from comments_book", null)
        if(rs.moveToFirst()){
            do {
                val uId = rs.getInt(1)
                val bId = rs.getInt(2)
                val status = rs.getInt(3)
                val title = rs.getString(4)
                val comment = Comment(uId, bId, status, title)
                list.add(comment)
            }while (rs.moveToNext())
        }
        return list
    }
}