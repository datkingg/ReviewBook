package com.example.rebook.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rebook.adapter.HomePostAdapter
import com.example.rebook.adapter.SavedBookAdapter
import com.example.rebook.databinding.FragmentSaveBookBinding
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookSaved
import com.example.rebook.model.BookSavedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaveBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveBookFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSaveBookBinding
    private lateinit var adapter: SavedBookAdapter
    private lateinit var helper: DatabaseHelper
    private lateinit var viewModel: BookSavedViewModel

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
        binding = FragmentSaveBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helper = DatabaseHelper(requireContext())
        val db = helper.readableDatabase

        adapter = SavedBookAdapter(requireContext(),loadDataFromDatabase())
        binding.rvSave.adapter = adapter
        binding.rvSave.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

    }
    private fun getUserId(): Int {
        helper = DatabaseHelper(requireContext())
        val db = helper.readableDatabase

        var email = arguments?.getString("email")
        val rs = db.rawQuery("select * from users where email = '$email'", null)
        rs.moveToFirst()
        return rs.getString(0).toInt()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaveBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaveBookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun loadDataFromDatabase(): List<BookSaved>{
        CoroutineScope(Dispatchers.IO).launch {
            val bookList = helper.getAllDataBookSaved(getUserId())
            withContext(Dispatchers.Main){
                adapter.setBookList(bookList)
            }
        }
        return helper.getAllDataBookSaved(getUserId())
    }
}