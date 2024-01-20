package com.example.rebook.fragmentAdmin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.rebook.R
import com.example.rebook.databinding.FragmentGeneralBinding
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
 * Use the [GeneralFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GeneralFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentGeneralBinding
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
        binding = FragmentGeneralBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        helper = DatabaseHelper(requireContext())

        val getData = getDataFromDatabase()
        val barDataSet = mutableListOf<IBarDataSet>()
        val labels = listOf("users","comments", "books", "post")
        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.mau10),
            ContextCompat.getColor(requireContext(), R.color.mau11),
            ContextCompat.getColor(requireContext(), R.color.mau12),
            ContextCompat.getColor(requireContext(), R.color.mau6)
        )
        for (i in labels.indices){
            val value = arrayListOf(BarEntry(i.toFloat(),getData[i]))
            val dataSet = BarDataSet(value,labels[i])
            dataSet.color = colors[i]
            barDataSet.add(dataSet)
        }

        val description = Description()
        description.setPosition(500f, 100f)
        description.text = "Biểu đồ tổng quát"
        description.textSize = 20f
        val data = BarData(barDataSet)
        binding.barCharGeneral.data = data
        binding.barCharGeneral.description = description
        binding.barCharGeneral.animateY(1500, Easing.EaseInOutQuad)
        binding.barCharGeneral.invalidate()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GeneralFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GeneralFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getDataFromDatabase(): ArrayList<Float>{
        val allUser = helper.getAllUserSquare()
        val allComment = helper.getAllCommentSquare()
        val allBook = helper.getAllBookSquare()
        val pots = helper.getAllPost()
        val allPosts = pots.size.toFloat()

        return arrayListOf(allUser, allComment, allBook,allPosts)
    }
}