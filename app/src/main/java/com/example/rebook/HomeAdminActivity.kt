package com.example.rebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.rebook.databinding.ActivityHomeAdminBinding
import com.example.rebook.fragmentAdmin.ManagerAccountFragment
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Users

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var emailAdmin:String
    private lateinit var helper: DatabaseHelper
    private lateinit var admin: Users
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = DatabaseHelper(this)
        val bundle = intent.extras

        if(bundle != null){
            emailAdmin = bundle.getString("email").toString()
        }

        val db = helper.readableDatabase
        val selection = "email = ?"
        val arr = arrayOf(emailAdmin)
        val rs = db.rawQuery("select * from users where $selection", arr)
        val bundleAdmin = Bundle()
        if(rs.moveToFirst()){
            val name = rs.getString(1)
            val gender = rs.getString(3)
            val birth = rs.getString(4)
            val password = rs.getString(5)
            admin = Users(name, emailAdmin,gender,birth,password)
            binding.txtName.text = "Xin chào! $name"
            bundleAdmin.putSerializable("admin", admin)
            bundleAdmin.putInt("idAdmin", rs.getInt(0))
        }



        binding.btnUsers.setOnClickListener {
            val intent = Intent(this,AccountManagenmentActivity::class.java)
            startActivity(intent)
        }
        binding.btnPosts.setOnClickListener {
            if(bundle != null){
                val intent = Intent(this, BaiVietActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        binding.btnSquare.setOnClickListener {
            val intent = Intent(this, ThongKeActivity::class.java)
            startActivity(intent)
        }

        binding.txtName.setOnClickListener {
            val intent = Intent(this, ProfileAdminActivity::class.java)
            intent.putExtras(bundleAdmin)
            startActivity(intent)

        }
    }
}