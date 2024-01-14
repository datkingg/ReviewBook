package com.example.rebook

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rebook.adapterAdmin.AdapterManagerAccount
import com.example.rebook.databinding.ActivityAccountManagenmentBinding
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.appcompat.widget.SearchView
import com.example.rebook.fragment.SubItemButtonClickListener
import com.example.rebook.model.Posts
import java.util.Locale

@Suppress("DEPRECATION")
class AccountManagenmentActivity : AppCompatActivity() {
    private lateinit var adapter: AdapterManagerAccount
    private lateinit var helper: DatabaseHelper
    private lateinit var binding: ActivityAccountManagenmentBinding
    private lateinit var rs: Cursor
    private lateinit var userList: List<Users>
    private lateinit var arrayList: ArrayList<Users>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountManagenmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        helper = DatabaseHelper(this)
        userList = mutableListOf()
        userList = loadUserDatabase()

        arrayList = arrayListOf()
        arrayList.addAll(userList)
        adapter = AdapterManagerAccount(arrayList, this, object : SubItemButtonClickListener {
                override fun onButtonClickUser(position: Int) {
                    val selection = "full_name = ?"
                    val arr = arrayOf(userList[position].fullname)
                    val ds = helper.readableDatabase
                    rs = ds.rawQuery("select * from users where $selection", arr)
                    rs.moveToFirst()
                    val idUser = rs.getInt(0)
                    val intent = Intent(this@AccountManagenmentActivity, XoaTaiKhoanActivity::class.java)
                    val user = Users(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5))
                    val bundle = Bundle()
                    bundle.putSerializable("user_current",user)
                    bundle.putInt("user_id", idUser)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    rs.close()
                }

                override fun onButtonClick(position: Int, buttonId: Int, posts: Posts) {
                    TODO("Not yet implemented")
                }

                override fun onButtonClickAdminPost(position: Int, buttonId: Int, posts: Posts) {
                    TODO("Not yet implemented")
                }

            })

//        Toast.makeText(this, "${loadUserDatabase()}", Toast.LENGTH_SHORT).show()

        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(
            this@AccountManagenmentActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter.notifyDataSetChanged()

        binding.btnSearchView.queryHint = "Tìm kiếm"
        binding.btnSearchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
//                val searchText = newText!!.toLowerCase(Locale.getDefault())
//                arrayList.clear()
//                if (
//                    searchText.isNotEmpty()
//                ) {
//                    userList.forEach {
//
//                        if (it.fullname.toLowerCase(Locale.getDefault())
//                                .contains(searchText)
//                        ) {
//                            arrayList.add(it)
//                        }
//
//                    }
//                    Log.d("YourTag", "Data changed. NotifyDataSetChanged called.")
//                    adapter.notifyDataSetChanged()
//                    if (arrayList.isEmpty()) {
//                        Toast.makeText(
//                            this@AccountManagenmentActivity,
//                            "No data",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    arrayList.clear()
//                    arrayList.addAll(userList)
//                    adapter.notifyDataSetChanged()
//                }
                return false
            }

        })

        binding.btnExit.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadUserDatabase(): List<Users> {
        CoroutineScope(Dispatchers.IO).launch {
            val userList = helper.getAllUser()
            withContext(Dispatchers.Main) {
                adapter.setUserList(userList)
            }
        }
        return helper.getAllUser()
    }
}