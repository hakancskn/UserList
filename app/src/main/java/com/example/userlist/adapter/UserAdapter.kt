package com.example.userlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.userlist.databinding.ItemUserBinding
import com.example.userlist.model.Person
import javax.inject.Inject

class UserAdapter @Inject constructor() :
    PagingDataAdapter<Person, UserAdapter.UserViewHolder>(COMPARATOR) {

    class UserViewHolder(var view: ItemUserBinding) : RecyclerView.ViewHolder(view.root){
        fun bind(item:Person) = with(view){
            user = item
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
    )



    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {

        val COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }


        }

    }


}