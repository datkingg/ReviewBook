package com.example.rebook.view.view_admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rebook.R
import com.example.rebook.databinding.ActivityThongKeBinding
import com.example.rebook.fragmentAdmin.CommentFragment
import com.example.rebook.fragmentAdmin.GeneralFragment
import com.example.rebook.helper.DatabaseHelper

@Suppress("DEPRECATION")
class ThongKeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThongKeBinding
    private lateinit var helper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThongKeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = DatabaseHelper(this)

        val subGeneralFragment = GeneralFragment()
        val subCommentFragment = CommentFragment()


        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.right_to_left,
                R.anim.exit_right_to_left,
                R.anim.left_to_right,
                R.anim.exit_left_to_right
            )
            replace(R.id.flSquare, subGeneralFragment)
            commit()
        }
        binding.btNavSquareAdmin.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.general -> {
                    supportFragmentManager.beginTransaction().apply {
                        setCustomAnimations(
                            R.anim.right_to_left,
                            R.anim.exit_right_to_left,
                            R.anim.left_to_right,
                            R.anim.exit_left_to_right
                        )
                        replace(R.id.flSquare, subGeneralFragment)
                        commit()
                    }
                }

                R.id.comment -> {
                    supportFragmentManager.beginTransaction().apply {
                        setCustomAnimations(
                            R.anim.right_to_left,
                            R.anim.exit_right_to_left,
                            R.anim.left_to_right,
                            R.anim.exit_left_to_right
                        )
                        replace(R.id.flSquare, subCommentFragment)
                        commit()
                    }
                }

                else -> {

                }
            }

            false
        }
        binding.btnExit.setOnClickListener {
            onBackPressed()
        }

//        val getData = getDataFromDatabase()
//        val barDataSet = mutableListOf<IBarDataSet>()
//        val labels = listOf("users","comments", "books", "post")
//        val colors = intArrayOf(
//            ContextCompat.getColor(this, R.color.mau10),
//            ContextCompat.getColor(this, R.color.mau11),
//            ContextCompat.getColor(this, R.color.mau12),
//            ContextCompat.getColor(this, R.color.mau6)
//        )
//        for (i in labels.indices){
//            val value = arrayListOf(BarEntry(i.toFloat(),getData[i]))
//            val dataSet = BarDataSet(value,labels[i])
//            dataSet.color = colors[i]
//            barDataSet.add(dataSet)
////            addRowToTable(binding.tableLayout, labels[i], getData[i].toString())
//        }

//        val description = Description()
//        description.setPosition(500f, 100f)
//        description.text = "Biểu đồ tổng quát"
//        description.textSize = 20f
//        val data = BarData(barDataSet)
//        binding.barChar.data = data
//        binding.barChar.description = description
//        binding.barChar.animateY(1500, Easing.EaseInOutQuad)
//        binding.barChar.invalidate()
    }

    private fun getDataFromDatabase(): ArrayList<Float> {
        val allUser = helper.getAllUserSquare()
        val allComment = helper.getAllCommentSquare()
        val allBook = helper.getAllBookSquare()
        val pots = helper.getAllPost()
        val allPosts = pots.size.toFloat()

        return arrayListOf(allUser, allComment, allBook, allPosts)
    }

//    private fun addRowToTable(tableLayout: TableLayout, name: String, count: String) {
//        // Tạo một TableRow mới
//        val tableRow = TableRow(this)
//
//        val txtName = TextView(this)
//        txtName.text = name
//        txtName.gravity = Gravity.CENTER
//        txtName.setPadding(8, 8, 8, 8)
//
//        val txtCount = TextView(this)
//        txtCount.text = count
//        txtCount.gravity = Gravity.CENTER
//        txtCount.setPadding(8, 8, 8, 8)
//
//        // Thêm TextView vào TableRow
//        tableRow.addView(txtName)
//        tableRow.addView(txtCount)
//
//        // Thêm TableRow vào TableLayout
//        tableLayout.addView(tableRow)
//    }
}