package com.example.rebook.fragmentAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.rebook.R
import com.example.rebook.databinding.FragmentCommentBinding
import com.example.rebook.helper.DatabaseHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCommentBinding
    private lateinit var helper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        helper = DatabaseHelper(requireContext())
        val getData = loadCommentFromDatabase()
        val barDataSet = mutableListOf<IBarDataSet>()
        val labels = listOf("1*","2*", "3*", "4*", "5*")
        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.red),
            ContextCompat.getColor(requireContext(), R.color.orange),
            ContextCompat.getColor(requireContext(), R.color.yellow),
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.blue)
        )
        for (i in labels.indices){
            val value = arrayListOf(BarEntry(i.toFloat(),getData[i]))
            val dataSet = BarDataSet(value,labels[i])
            dataSet.color = colors[i]
            barDataSet.add(dataSet)
        }
        val description = Description()
        description.setPosition(750f, 100f)
        description.text = "Biểu đồ đánh giá người dùng"
        description.textSize = 20f
        val data = BarData(barDataSet)
        binding.barCharComment.data = data
        binding.barCharComment.description = description
        binding.barCharComment.animateY(1500, Easing.EaseInOutQuad)
        binding.barCharComment.invalidate()
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadCommentFromDatabase(): ArrayList<Float>{
        val list = helper.getStatusCommentSquare()
        val arrFloat = arrayListOf<Float>()

        var mot = 0
        var hai = 0
        var ba = 0
        var bon = 0
        var nam = 0
        for (item in list){
            if (item.status == 1){
                mot++
            }else if (item.status == 2){
                hai++
            }else if (item.status == 3){
                ba++
            }else if(item.status == 4){
                bon++
            }else if (item.status == 5){
                nam++
            }
        }
        arrFloat.add(mot.toFloat())
        arrFloat.add(hai.toFloat())
        arrFloat.add(ba.toFloat())
        arrFloat.add(bon.toFloat())
        arrFloat.add(nam.toFloat())
        return arrFloat
    }
}