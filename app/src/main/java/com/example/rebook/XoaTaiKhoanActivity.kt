package com.example.rebook

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.rebook.databinding.ActivityXoaTaiKhoanBinding
import com.example.rebook.factory.UserViewModelFactory
import com.example.rebook.model.UserViewModel
import com.example.rebook.model.Users

@Suppress("DEPRECATION")
class XoaTaiKhoanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityXoaTaiKhoanBinding
    private lateinit var userCurrent: Users
    private var idUser: Int? = null
    private lateinit var viewModel: UserViewModel
    private lateinit var alertDialog:AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityXoaTaiKhoanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (!bundle?.isEmpty!!) {
            userCurrent = bundle.getSerializable("user_current") as Users
            idUser = bundle.getInt("user_id")
        }


        binding.tvTenTaiKhoan.text = userCurrent.fullname
        binding.tvEmail.text = userCurrent.email
        binding.tvGioiTinh.text = userCurrent.gender
        binding.tvHoTen.text = userCurrent.fullname
        binding.tvNgaySinh.text = userCurrent.birthday
        binding.tvMatKhau.text = userCurrent.password

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Xoá người dùng")
        alertDialogBuilder.setMessage("Bạn chắc chắn muốn xoá?")

// Thiết lập các nút (ví dụ: nút tích cực và nút tiêu cực)
        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            val factory = UserViewModelFactory(this)
            viewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
            idUser?.let { it1 -> viewModel.deleteUser(it1) }
            onBackPressed()
        }

        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->

        }

// Tạo và hiển thị AlertDialog


        binding.btnXoaTaiKhoan.setOnClickListener {
            alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        binding.btnExit.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onDestroy() {
        if(alertDialog != null && alertDialog.isShowing){
            alertDialog.dismiss()
        }
        super.onDestroy()
    }
}