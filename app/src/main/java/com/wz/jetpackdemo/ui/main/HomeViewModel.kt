package com.wz.jetpackdemo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wz.jetpackdemo.model.User
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    val userLiveData= MutableLiveData<User>()
    init{
        userLiveData.postValue(User("wz", 18))
    }

    fun getUserInfo() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000L)
            val user = userLiveData.getValue()
            user?.run{
                age = 20
                name = "wz"
            }
            withContext(Dispatchers.Main){
                userLiveData.setValue(user)
            }
        }
    }
}