package com.example.rebook

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.rebook.databinding.ActivityBaiVietMoiBinding
import com.example.rebook.helper.DatabaseHelper
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

@Suppress("DEPRECATION")
class BaiVietMoiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaiVietMoiBinding
    private val REQUEST_CODE_CAMERA = 123
    private val REQUEST_CODE_FOLDER = 456
    private lateinit var helper: DatabaseHelper
    private lateinit var rs: Cursor

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaiVietMoiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        helper = DatabaseHelper(this)
        //gọi nút chụp anhr
        binding.btnCamera.setOnClickListener {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA
            )
        }
//        gọi nút thư viện
        binding.btnFolder.setOnClickListener {

            if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                    REQUEST_CODE_FOLDER
                )
            } else{
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_FOLDER)
            }

        }



        binding.btnSave.setOnClickListener {
            val bitmapDrawable: Drawable? = binding.imgBook.drawable

            if (
                TextUtils.isEmpty(binding.edTenSach.text)
            ) {
                binding.edTenSach.error = "Ten sach khong duoc de trong"
                binding.edTenSach.focusable
            } else if (
                TextUtils.isEmpty(binding.edTacGiaMoi.text)
            ) {
                binding.edTacGiaMoi.error = "Ten tac gia khong duoc de trong"
                binding.edTacGiaMoi.focusable
            } else if (
                TextUtils.isEmpty(binding.edNhaXuatBanMoi.text)
            ) {
                binding.edNhaXuatBanMoi.error = "Ten NXB khong duoc de trong"
                binding.edNhaXuatBanMoi.focusable
            } else if (
                TextUtils.isEmpty(binding.edTheLoaiMoi.text)
            ) {
                binding.edTheLoaiMoi.error = "Ten the loai khong duoc de trong"
                binding.edTheLoaiMoi.focusable
            } else if (
                TextUtils.isEmpty(binding.edMoTaMoi.text)
            ) {
                binding.edMoTaMoi.error = "Mo ta khong duoc de trong"
                binding.edMoTaMoi.focusable
            }else if(bitmapDrawable == null){
                Toast.makeText(this, "Sach chua co hinh anh", Toast.LENGTH_SHORT).show()

            } else {
                val bitmap = (bitmapDrawable as BitmapDrawable).bitmap
                val byteArray = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
                val imgBook: ByteArray = byteArray.toByteArray()
                var check  = 0
                val db = helper.readableDatabase
                helper.insertCategory(
                    binding.edTheLoaiMoi.text.toString()
                )
                val selectionCate = "category_name = ?"
                val arr1 = arrayOf(binding.edTheLoaiMoi.text.toString().trim())
                rs = db.rawQuery(
                    "select * from categories where $selectionCate", arr1
                )
                if (rs.moveToFirst()) {
                    helper.insertBooks(
                        imgBook,
                        binding.edTenSach.text.toString().trim(),
                        binding.edTacGiaMoi.text.toString().trim(),
                        rs.getInt(0),
                        binding.edNhaXuatBanMoi.text.toString().trim()
                    )
                    rs.close()
                }

                val selectionBook = "book_name = ?"
                val arr2 = arrayOf(binding.edTenSach.text.toString().trim())
                rs = db.rawQuery(
                    "select * from books where $selectionBook",
                    arr2
                )
                if (rs.moveToFirst()) {
                    var bookId = rs.getInt(0)
                    rs.close()
                    if (bundle != null) {
                        val adminEmail = bundle.getString("email")
                        val selectionAdmin = "email = ?"
                        val arr3 = arrayOf(adminEmail)
                        val rs = db.rawQuery("select * from users where $selectionAdmin",
                            arr3)
                        if(rs.moveToFirst()){
                            val adminId = rs.getInt(0)
                            check = helper.insertPost(
                                adminId, bookId,
                                binding.edMoTaMoi.text.toString()
                            ).toInt()
                            Toast.makeText(this, "$check", Toast.LENGTH_SHORT).show()
                            rs.close()
                        }

                    }
                }
                if(check != 0){
                    finish()
                }
            }
        }
        binding.btnExit.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            val bitmap: Bitmap = data.extras?.get("data") as Bitmap
            binding.imgBook.setImageBitmap(bitmap)
        }
        else if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            try {
                var inputStream = uri?.let { contentResolver.openInputStream(it) }
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imgBook.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CODE_CAMERA)
                }
            }

            REQUEST_CODE_FOLDER -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/*"
                    startActivityForResult(intent, REQUEST_CODE_FOLDER)
                }
                else {
                    // Xử lý khi người dùng từ chối quyền
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                    openAppSettings()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}