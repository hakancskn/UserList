package com.example.userlist.viewmodel

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.userlist.model.Person
import com.example.userlist.model.ProcessResult
import com.example.userlist.paging.UserPaginationSource
import com.example.userlist.paging.UserRepository
import com.example.userlist.service.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(): ViewModel() {

    var nextKey: String? = "0"
    var itemCount: Int = 0
    var isRefreshing = false
    var recyclerViewPosition: Int = 0

    val userError = MutableLiveData<String>()

    private  var _userFlow: MutableLiveData<PagingData<Person>>? = null

    val usersFlow: MutableLiveData<PagingData<Person>>?
        get() = _userFlow




    init {
        launchPagingAsync()
    }



     private fun launchPagingAsync() {
        viewModelScope.launch {
            try {

                 _userFlow =  UserRepository().getDataSourceStream(this@UserViewModel)
                     .asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
                     .cachedIn(viewModelScope) as MutableLiveData<PagingData<Person>>

            } catch (ex: Exception) {
                println("Error ${ex.localizedMessage}")
            }
        }
    }


    suspend fun getUserList(): ProcessResult {
        val dataSource = DataSource()
        return dataSource.fetch(nextKey)


    }



}