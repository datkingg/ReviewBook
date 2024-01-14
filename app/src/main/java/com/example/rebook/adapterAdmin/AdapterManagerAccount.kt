package com.example.rebook.adapterAdmin

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.rebook.AccountManagenmentActivity
import com.example.rebook.databinding.UserItemBinding
import com.example.rebook.fragment.SubItemButtonClickListener
import com.example.rebook.helper.DatabaseHelper
import com.example.rebook.model.Users
import java.util.Locale

class AdapterManagerAccount(
    private var ds: List<Users> = emptyList(),
    private val context: Context,
    private val listener: SubItemButtonClickListener
) : RecyclerView.Adapter<AdapterManagerAccount.AccountHolder>(), Filterable {
    private lateinit var binding: UserItemBinding
    private lateinit var helper: DatabaseHelper
    private lateinit var rs:Cursor
    private lateinit var ds2: List<Users>

    inner class AccountHolder(binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Users) {
            helper = DatabaseHelper(context)
            val db = helper.readableDatabase
            val query = "select * from users where email = '${user.email}'"
            rs = db.rawQuery(query,null)
            if(rs.moveToFirst()){
                binding.txtID.text = rs.getInt(0).toString()
                binding.txtEmail.text = user.email
                binding.txtName.text = user.fullname
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = UserItemBinding.inflate(inflater)
        return AccountHolder(binding)
    }

    override fun getItemCount(): Int {
        return ds.size
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        val currentUser = ds[position]
        holder.itemView.setOnClickListener {
            listener.run { onButtonClickUser(position) }
        }
        return holder.bind(currentUser)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(users: List<Users>){
        ds = users
        ds2 = users
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var strSearch = constraint.toString()
                ds = if(strSearch.isEmpty()){
                    ds2
                }else{
                    val list: ArrayList<Users> = arrayListOf()
                    ds2.forEach{
                        if(it.fullname.lowercase(Locale.getDefault()).contains(strSearch.toLowerCase(Locale.getDefault()))){
                            list.add(it)
                        }
                    }
                    list
                }
                val filterResults = FilterResults()
                filterResults.values = ds
                return filterResults

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ds = results?.values as List<Users>
                notifyDataSetChanged()
            }

        }
        return filter
    }
}