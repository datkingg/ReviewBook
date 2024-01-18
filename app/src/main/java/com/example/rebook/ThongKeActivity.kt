package com.example.rebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.rebook.databinding.ActivityThongKeBinding
import com.example.rebook.helper.DatabaseHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class ThongKeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThongKeBinding
    private lateinit var helper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThongKeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = DatabaseHelper(this)
        val getData = getDataFromDatabase()
        val barDataSet = mutableListOf<IBarDataSet>()
        var tableData:List<Any> = mutableListOf()
        val labels = listOf("users","comments", "books", "post")
        val colors = intArrayOf(
            ContextCompat.getColor(this, R.color.mau10),
            ContextCompat.getColor(this, R.color.mau11),
            ContextCompat.getColor(this, R.color.mau12),
            ContextCompat.getColor(this, R.color.mau6)
        )
        for (i in labels.indices){
            val value = arrayListOf(BarEntry(i.toFloat(),getData[i]))
            val dataSet = BarDataSet(value,labels[i])
            dataSet.color = colors[i]
            barDataSet.add(dataSet)
            tableData = listOf(
                Pair(labels[i],getData[i])
            )
            addRowToTable(binding.tableLayout, labels[i], getData[i].toString())
        }

        val description = Description()
        description.setPosition(500f, 100f)
        description.text = "Biểu đồ tổng quát"
        description.textSize = 20f
        val data = BarData(barDataSet)
        binding.barChar.data = data
        binding.barChar.description = description
        binding.barChar.animateY(1500, Easing.EaseInOutQuad)
        binding.barChar.invalidate()
    }

    private fun getDataFromDatabase(): ArrayList<Float>{
        val allUser = helper.getAllUserSquare()
        val allComment = helper.getAllComment()
        val allBook = helper.getAllBookSquare()
        val pots = helper.getAllPost()
        val allPosts = pots.size.toFloat()

        return arrayListOf(allUser, allComment, allBook,allPosts)
    }

    private fun addRowToTable(tableLayout: TableLayout, name: String, count: String) {
        // Tạo một TableRow mới
        val tableRow = TableRow(this)

        val txtName = TextView(this)
        txtName.text = name
        txtName.gravity = Gravity.CENTER
        txtName.setPadding(8, 8, 8, 8)

        val txtCount = TextView(this)
        txtCount.text = count
        txtCount.gravity = Gravity.CENTER
        txtCount.setPadding(8, 8, 8, 8)

        // Thêm TextView vào TableRow
        tableRow.addView(txtName)
        tableRow.addView(txtCount)

        // Thêm TableRow vào TableLayout
        tableLayout.addView(tableRow)
    }
}