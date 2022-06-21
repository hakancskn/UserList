package com.example.userlist.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.userlist.adapter.UserAdapter
import com.example.userlist.databinding.NetworkStateItemBinding

class UserLoadStateAdapter(private val adapter: UserAdapter?) :
    LoadStateAdapter<UserLoadStateAdapter.NetworkStateItemViewHolder>() {
    inner class NetworkStateItemViewHolder(
        binding: NetworkStateItemBinding,
        private val retryCallback: () -> Unit,
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        private val progressBar = binding.progressBar
        private val rootView = binding.viewSize
        private val retry = binding.retryButton
            .also {
                it.setOnClickListener { retryCallback() }
            }

        fun bindTo(loadState: LoadState) {

            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error

            (loadState as? LoadState.Error)?.error?.let {

            }
        }
    }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ):NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context))) { adapter?.retry() }
    }

}