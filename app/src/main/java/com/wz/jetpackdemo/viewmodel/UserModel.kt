package com.wz.jetpackdemo.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.wz.jetpackdemo.model.User
import com.wz.jetpackdemo.repository.DataRepository
import kotlinx.coroutines.*
import java.util.function.Function

class UserModel : ViewModel() {
    val userLiveData= MutableLiveData<User>()
    var isFirstLaunch by DataRepository::isFirstLaunch

    val userNameLiveData: LiveData<String> = Transformations.map(userLiveData){
        user -> "${user.name}"
    }

    val userId: LiveData<Int> = MutableLiveData<Int>()
    val user = Transformations.switchMap(userId){
        id->getUser(id)
    }


    init{
        userLiveData.postValue(User("wz", 18))
    }

    private fun getUser(id: Int):LiveData<User> {
        return MutableLiveData<User>()
    }

    fun getUserInfo() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000L)
            val user = userLiveData.getValue()
            user?.run{
                age = 20
                name = "wz"
            }
            userLiveData.postValue(user)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun <T, R> onUserModify(function: Function<T, R>) {
        val t:T? = null
        function.apply(t!!)
    }

    override fun onCleared() {
        super.onCleared()

    }

}