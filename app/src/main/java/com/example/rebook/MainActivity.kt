package com.example.rebook

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.rebook.databinding.ActivityMainBinding
import com.example.rebook.fragment.HomeFragment
import com.example.rebook.fragment.PersonalFragment
import com.example.rebook.fragment.SaveBookFragment
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var db: SQLiteDatabase
    lateinit var rs: Cursor
    lateinit var adapter: SimpleCursorAdapter
    private lateinit var mEmail: String
    private val dataViewModel: UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subPersonalFragment = PersonalFragment()
        val subSaveBookFragment = SaveBookFragment()
        val subHomeFragment = HomeFragment()

        val helper = DatabaseHelper(applicationContext)
        db = helper.readableDatabase

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flHome, subHomeFragment)
            commit()
        }

        val bundle = dataTranfer()
        if(bundle != null){
            subPersonalFragment.arguments = bundle
            subHomeFragment.arguments = bundle
            subSaveBookFragment.arguments = bundle
        }
        binding.btNavUserHome.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btnHome -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flHome, subHomeFragment)
                        commit()
                    }
                }
                R.id.btnProfile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flHome, subPersonalFragment)
                        commit()
                    }
                }
                R.id.btnSave -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flHome, subSaveBookFragment)
                        commit()
                    }
                }
                else -> {

                }
            }
            true
        }
    }

    private fun dataTranfer(): Bundle {
        val i = intent
        val bd = i.extras
        val bundle = Bundle()
        if (bd != null) {
            val emailInput = bd.getString("email")
            rs = db.rawQuery("select * from users where email = '$emailInput'", null)
            rs.moveToFirst()
            val id = rs.getInt(0)
            val name = rs.getString(1)
            val email = rs.getString(2)
            val gender = rs.getString(3)
            val birthday = rs.getString(4)
            val pass = rs.getString(5)
            Toast.makeText(this, "$email", Toast.LENGTH_SHORT).show()
            bundle.putInt("id", id)
            bundle.putString("name", name)
            bundle.putString("email", email)
            bundle.putString("gender", gender)
            bundle.putString("birth", birthday)
            bundle.putString("pass", pass)
            return bundle
        } else {
            Toast.makeText(this@MainActivity, "no info", Toast.LENGTH_SHORT).show()
        }
        return bundle
    }

    fun getEmail(): String {
        return mEmail
    }

}