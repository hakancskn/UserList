package com.example.userlist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userlist.model.FetchResponse
import com.example.userlist.model.Person
import com.example.userlist.service.DataSource
import com.example.userlist.service.FetchCompletionHandler

class UserViewModel : ViewModel() {
    val userList = MutableLiveData<List<Person>>()
    val userLoading = MutableLiveData<Boolean>()

    fun getUserList() {
        val dataSource: DataSource = DataSource()
        dataSource.fetch(null, completionHandler = { fetchResponse, fetchError ->
            fetchResponse.let {
                userList.postValue(fetchResponse?.people)
            }
        })
    }
}