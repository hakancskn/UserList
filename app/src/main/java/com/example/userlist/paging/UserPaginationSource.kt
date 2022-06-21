package com.example.userlist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.userlist.model.Person
import com.example.userlist.viewmodel.UserViewModel

class UserPaginationSource(private val viewModel: UserViewModel) : PagingSource<Int, Person>() {
    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return try {
            val response = viewModel.getUserList()

            response.fetchError?.errorDescription?.let {
                viewModel.userError.postValue(it)
            }

            viewModel.nextKey = response.fetchResponse?.next
            var list: List<Person> = listOf()
            response.fetchResponse?.people?.let {
                list = it
            }

            val nextKey = if (list.isEmpty()) {
                null
            } else {
                viewModel.itemCount.plus(list.size)
            }
            viewModel.itemCount = nextKey ?: 0
            LoadResult.Page(
                data = list,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}