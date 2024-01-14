package com.example.rebook

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.rebook.databinding.ActivityRegisterBinding
import com.example.rebook.helper.DatabaseHelper
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDatabase
    private lateinit var rs: Cursor
    private lateinit var binding: ActivityRegisterBinding
    val today = Calendar.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var helper = DatabaseHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("SELECT * FROM users", null)

        binding.btnDate.setText(today.get(Calendar.DAY_OF_MONTH).toString())
        binding.btnDate.setOnClickListener {
            val startDay = today.get(Calendar.DAY_OF_MONTH)
            val startMonth = today.get(Calendar.MONTH)
            val startYear = today.get(Calendar.YEAR)
            DatePickerDialog(this@RegisterActivity,DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.edBirthDay.setText("${dayOfMonth}/${month+1}/$year")
            },startYear,startMonth,startDay).show()
        }



        binding.btnSign.setOnClickListener {
            if(TextUtils.isEmpty(binding.edName.text)){
                binding.edName.error = "Full name can not be empty"
                binding.edName.focusable
            }
            else if(TextUtils.isEmpty(binding.edEmailSign.text)){
                binding.edName.error = "Email can not be empty"
                binding.edName.focusable
            }
            else if(TextUtils.isEmpty(binding.edBirthDay.text)){
                binding.edName.error = "Birthday can not be empty"
                binding.edName.focusable
            }
            else if(TextUtils.isEmpty(binding.edPassSign.text)){
                binding.edName.error = "Password can not be empty"
                binding.edName.focusable
            }
            else if(!binding.radNam.isChecked && !binding.radNu.isChecked){
                Toast.makeText(this@RegisterActivity, "Bạn chưa chọn giới tính", Toast.LENGTH_SHORT ).show()
            }
            else{
                var s:String
                if(binding.radNu.isChecked){
                    s = binding.radNu.text.toString()
                }
                else{
                    s = binding.radNam.text.toString()
                }
                isRegisterUser(binding.edName.text.toString(),
                    binding.edEmailSign.text.toString(),
                    s,
                    binding.edBirthDay.text.toString(),
                    binding.edPassSign.text.toString())
                Toast.makeText(this@RegisterActivity, "Register successful", Toast.LENGTH_SHORT).show()
                val i = Intent(applicationContext,LoginActivity::class.java)
                startActivities(arrayOf(i))
                binding.edName.setText("")
                binding.radioGroup.clearCheck()
                binding.edBirthDay.setText("")
                binding.edPassSign.setText("")
                binding.edEmailSign.setText("")
            }
        }

        binding.btnLogin.setOnClickListener {
            val i = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivities(arrayOf(i))
        }
    }

    private fun isRegisterUser(full_name:String,
                               email: String,
                               gender:String,
                               birthday: String,
                               password: String): Boolean{

        var cv = ContentValues()
        cv.put("FULL_NAME",full_name)
        cv.put("EMAIL", email)
        cv.put("GENDER",gender)
        cv.put("BIRTHDAY", birthday)
        cv.put("PASSWORD", password)
        cv.put("status",0)
        db.insert("users",null,cv)
        rs.requery()
        return true
    }
}