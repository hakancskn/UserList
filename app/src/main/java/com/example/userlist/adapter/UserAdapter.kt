package com.example.userlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.userlist.R
import com.example.userlist.databinding.ItemUserBinding
import com.example.userlist.model.Person

class UserAdapter(var userList: ArrayList<Person>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(var view:ItemUserBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemUserBinding>(
            inflater,
            R.layout.item_user,
            parent,
            false
        )
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.view.user = userList[position]
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}