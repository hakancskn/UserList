package com.example.userlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.userlist.adapter.UserAdapter
import com.example.userlist.databinding.FragmentUserBinding
import com.example.userlist.paging.UserLoadStateAdapter
import com.example.userlist.utils.setErrorMessage
import com.example.userlist.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUserBinding


    private var userAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userAdapter = UserAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentUserBinding.inflate(inflater, container, false).also {
            binding = it
        }
        clearCash()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        binding.apply {
            userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            userRecyclerView.setHasFixedSize(true)

            userRecyclerView.adapter = userAdapter?.withLoadStateHeaderAndFooter(
                header = UserLoadStateAdapter(userAdapter),
                footer = UserLoadStateAdapter(userAdapter)
            )

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                userAdapter?.loadStateFlow?.collectLatest { loadStates ->
                    swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
                    if (viewModel.isRefreshing)
                        viewModel.isRefreshing = false

                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.usersFlow?.observe(viewLifecycleOwner) { pagedData ->
                    userAdapter?.submitData(viewLifecycleOwner.lifecycle, pagedData)
                    userAdapter?.notifyDataSetChanged()

                }
            }

            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                userAdapter?.loadStateFlow
                    ?.distinctUntilChangedBy { it.refresh }
                    ?.filter { it.refresh is LoadState.NotLoading }
                    ?.collect {

                        if (viewModel.recyclerViewPosition == 0)
                            userRecyclerView.scrollToPosition(viewModel.recyclerViewPosition)
                    }
            }
            userAdapter?.addLoadStateListener { loadState ->

                if (loadState.refresh is LoadState.NotLoading &&
                    loadState.refresh.endOfPaginationReached) {
                    userAdapter?.itemCount?.let { emptyDataLayoutVisibility(it) }
                }
            }
        }

        viewModel.userError.observe(viewLifecycleOwner) { error ->
            error?.let {

                setErrorMessage(
                    context = requireContext(),
                    description = it,
                    positiveClick = { _, dialog ->
                        dialog.dismiss()
                        callSwipeToRefresh()
                    }
                )


            }
        }



        binding.swipeRefreshLayout.setOnRefreshListener {
            callSwipeToRefresh()
        }

    }


    private fun FragmentUserBinding.emptyDataLayoutVisibility(count: Int) {
        if (count < 1) {
            userRecyclerView.visibility = View.GONE
            userError.visibility = View.VISIBLE
        } else {
            userRecyclerView.visibility = View.VISIBLE
            userError.visibility = View.GONE
        }
    }



    private fun callSwipeToRefresh() {

        binding.userRecyclerView.smoothScrollToPosition(0)
        userAdapter?.refresh()
    }


    private fun prepareRecyclerView() {

        binding.apply {
            userRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                setItemViewCacheSize(0)
                adapter = userAdapter

            }
        }


    }

    override fun onDestroy() {
        userAdapter = null
        viewModelStore.clear()
        super.onDestroy()
    }

    override fun onDestroyView() {
        viewModel.userError?.removeObservers(viewLifecycleOwner)
        viewModel.usersFlow?.removeObservers(viewLifecycleOwner)

        super.onDestroyView()
    }

    private fun clearCash() {
        viewModel.usersFlow?.removeObservers(this)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }
    private fun refreshData() {
        viewModel.isRefreshing = true
        viewModel.recyclerViewPosition = 0
        userAdapter?.refresh()
    }
}