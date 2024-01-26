package com.example.rebook.view.view_admin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.databinding.ActivityProfileAdminBinding
import com.example.rebook.factory.UserViewModelFactory
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Users
import com.example.rebook.view.sign_in.LoginActivity
import com.example.rebook.view_model.UserViewModel


@Suppress("DEPRECATION")
class ProfileAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileAdminBinding
    private lateinit var helper: DatabaseHelper
    private lateinit var admin: Users
    private lateinit var viewModel: UserViewModel
    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = DatabaseHelper(this)

        val factory = UserViewModelFactory(this)
        viewModel = ViewModelProvider(
            this, factory
        )[UserViewModel::class.java]

        var id = 0
        val bundle = intent.extras
        if (bundle != null) {
            admin = bundle.getSerializable("admin") as Users
            binding.edEmailAd.setText(admin.email)
            binding.edTenAdmin.setText(admin.fullname)
            binding.edGioiTinhAd.setText(admin.gender)
            binding.edNgaySinhAd.setText(admin.birthday)
            binding.edMatKhauAd.setText(admin.password)
            id = bundle.getInt("idAdmin")
        }


        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Lưu thay đổi")
        alertDialogBuilder.setMessage("Bạn chắc chắn muốn lưu?")

// Thiết lập các nút (ví dụ: nút tích cực và nút tiêu cực)
        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            val name = binding.edTenAdmin.text.toString()
            val email = binding.edEmailAd.text.toString()
            val gender = binding.edGioiTinhAd.text.toString()
            val birth = binding.edNgaySinhAd.text.toString()
            val mk = binding.edMatKhauAd.text.toString()
            viewModel.updateUserData(id, name, email, gender, birth, mk)
        }
        alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }
        alertDialog = alertDialogBuilder.create()

        binding.btnThoat.setOnClickListener {
            onBackPressed()
        }

        binding.btnLuuThongTin.setOnClickListener {
            showAlertDialog(
                "Lưu thay đổi",
                "Bạn chắc chắn muốn lưu?"
            ) { _, _ ->
                val name = binding.edTenAdmin.text.toString()
                val email = binding.edEmailAd.text.toString()
                val gender = binding.edGioiTinhAd.text.toString()
                val birth = binding.edNgaySinhAd.text.toString()
                val mk = binding.edMatKhauAd.text.toString()
                viewModel.updateUserData(id, name, email, gender, birth, mk)
            }

        }

        binding.btnLogout.setOnClickListener {

            showAlertDialog("Đăng xuất", "Bạn chắc chắn muốn đăng xuất?") { _, _ ->
                performLogout()
            }

        }

//        showDialogButton1.setOnClickListener(View.OnClickListener {
//            showAlertDialog(
//                "Title 1", "Message 1"
//            ) { dialog, which -> // Xử lý khi người dùng nhấn nút OK trong AlertDialog 1
//                // Ví dụ: Hiển thị thông báo
//                showToast("Clicked OK in Dialog 1")
//            }
//        })

    }

    override fun onDestroy() {
        if (::alertDialog.isInitialized && alertDialog.isShowing) {
            alertDialog.dismiss()
        }
        super.onDestroy()
    }

    private fun performLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        positiveClickListener: DialogInterface.OnClickListener
    ) {
        alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", positiveClickListener)
            .setNegativeButton("Cancel") { _, _ ->
                // Xử lý khi người dùng nhấn nút Cancel
            }
            .create()
        alertDialog.show()
    }
}