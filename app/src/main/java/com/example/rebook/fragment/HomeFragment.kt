package com.example.rebook.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.BinhLuanActivity
import com.example.rebook.adapter.HomePostAdapter
import com.example.rebook.databinding.FragmentHomeBinding
import com.example.rebook.factory.BookSavedFactory
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.BookSavedViewModel
import com.example.rebook.model.BookViewModel
import com.example.rebook.model.Posts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var viewModel: BookViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var rv: RecyclerView
    private lateinit var adapter: HomePostAdapter
    private lateinit var helper: DatabaseHelper
    private lateinit var binding: FragmentHomeBinding

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        helper = DatabaseHelper(requireContext())
        val db = helper.readableDatabase

        var email = arguments?.getString("email")
        val rs = db.rawQuery("select * from users where email = '$email'", null)
        rs.moveToFirst()
        var userId = rs.getString(0).toInt()
        val subItemButtonClickListener = object : SubItemButtonClickListener {
            override fun onButtonClickUser(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onButtonClick(position: Int, buttonId: Int, posts: Posts) {
                when (buttonId) {
                    1 -> {
                        var bookId = posts.bookId
                        val viewModelFactory = BookSavedFactory(requireContext())
                        viewModel = ViewModelProvider(
                            requireActivity(),
                            viewModelFactory
                        )[BookSavedViewModel::class.java]
                        viewModel.insertBookSaved(bookId, userId)
                        Toast.makeText(requireContext(), "luu thanh cong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    2 -> {
                        val intent = Intent(requireContext(), BinhLuanActivity::class.java)

                        val bundle = Bundle().apply {
                            putInt("bookId", posts.bookId)
                            putInt("userId", userId)
                        }
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                }
            }

            override fun onButtonClickAdminPost(position: Int, buttonId: Int, posts: Posts) {
                TODO("Not yet implemented")
            }

        }

        adapter =
            HomePostAdapter(requireContext(), loadDataFromDatabase(), subItemButtonClickListener)
        loadDataFromDatabase()
        binding.rvHome.adapter = adapter
        binding.rvHome.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun loadDataFromDatabase(): List<Posts> {
        CoroutineScope(Dispatchers.IO).launch {
            val postList = helper.getAllPost()
            withContext(Dispatchers.Main) {
                adapter.setPostList(postList)
            }
        }

        return helper.getAllPost()
    }
}