package com.example.userlist.paging


import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.userlist.model.Person
import com.example.userlist.viewmodel.UserViewModel
import kotlinx.coroutines.flow.Flow

class UserRepository {

    fun getDataSourceStream(viewModel: UserViewModel): Flow<PagingData<Person>> {
        return Pager(
            config = PagingConfig(
                pageSize = USER_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = USER_PAGE_SIZE,
                prefetchDistance = 7,
            )
        ){
            UserPaginationSource(viewModel)
        }.flow
    }

    companion object {
        private const val USER_PAGE_SIZE = 20
    }
}