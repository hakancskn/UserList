package com.example.userlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userlist.R
import com.example.userlist.adapter.UserAdapter
import com.example.userlist.model.Person
import com.example.userlist.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user.*


class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private val userAdapter = UserAdapter(arrayListOf())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        prepareRecyclerView()
        viewModel.getUserList()

    }

    private fun prepareRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        user_recyclerView.layoutManager = layoutManager

        user_recyclerView.adapter = userAdapter
    }

    fun observeLiveData() {
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.userList.observe(viewLifecycleOwner) { userList ->
            userList.let {
                userAdapter.userList = it as ArrayList<Person>
                userAdapter.notifyDataSetChanged()
            }

        }
    }

}