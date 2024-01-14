package com.example.rebook.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rebook.LoginActivity
import com.example.rebook.R
import com.example.rebook.databinding.FragmentPersonalBinding
import com.example.rebook.factory.UserViewModelFactory
import com.example.rebook.model.UserViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PersonalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class PersonalFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPersonalBinding
    private var isEditingEnabled = false
    private lateinit var editTexts: List<EditText>
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun disableEditing(binding: FragmentPersonalBinding) {
        editTexts.forEach{editText ->
            editText.inputType = InputType.TYPE_NULL
            editText.keyListener = null
        }
        isEditingEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun enableEditing(binding: FragmentPersonalBinding){
        editTexts.forEach { editText ->
            editText.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE
            editText.keyListener = EditText(context).keyListener
        }
        binding.edNamePro.requestFocus()
        isEditingEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater,container,false)

        editTexts = listOf(
            binding.edNamePro,
            binding.edEmailPro,
            binding.edBirthPro,
            binding.edGenderPro,
            binding.edPassPro
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name")
        val email = arguments?.getString("email")
        val gender = arguments?.getString("gender")
        val birthday = arguments?.getString("birth")
        val password = arguments?.getString("pass")
        val id = arguments?.getInt("id")

        binding.edNamePro.setText(name)
        binding.edEmailPro.setText(email)
        binding.edGenderPro.setText(gender)
        binding.edBirthPro.setText(birthday)
        binding.edPassPro.setText(password)


        disableEditing(binding)
        binding.imgChangeSave.setOnClickListener {

            if (isEditingEnabled){
                binding.imgChangeSave.setImageResource(R.drawable.ic_change_foreground)
                disableEditing(binding)
            }
            else{
                binding.imgChangeSave.setImageResource(R.drawable.check_solid)
                enableEditing(binding)
                val viewModelFactory = UserViewModelFactory(requireContext())
                viewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
                if (id != null) {
                    viewModel.updateUserData(
                        id,
                        binding.edNamePro.text.toString().trim(),
                        binding.edEmailPro.text.toString().trim(),
                        binding.edGenderPro.text.toString().trim(),
                        binding.edBirthPro.text.toString().trim(),
                        binding.edPassPro.text.toString().trim(),
                    )
                }
            }
        }

        binding.btnLogOut.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PersonalFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PersonalFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
