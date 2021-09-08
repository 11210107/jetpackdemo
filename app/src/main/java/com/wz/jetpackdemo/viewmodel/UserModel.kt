package com.wz.jetpackdemo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.wz.jetpackdemo.model.User
import kotlinx.coroutines.*

class UserModel : ViewModel() {
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