package com.wz.jetpackdemo.ui.main

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wz.jetpackdemo.annotation.Range
import com.wz.jetpackdemo.annotation.Report
import com.wz.jetpackdemo.model.Stock
import com.wz.jetpackdemo.model.User
import com.wz.jetpackdemo.security.AESEncrypt
import kotlinx.coroutines.*
import java.lang.reflect.Proxy
import java.util.*

class HomeViewModel : ViewModel() {
    val userLiveData= MutableLiveData<User>()
    init{
        userLiveData.value= User("wz", 18)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        AESEncrypt.base64()
        AESEncrypt.md5()
        AESEncrypt.hmacMD5()
        AESEncrypt.hmac()
        AESEncrypt.aes()
        reflection()
        proxy()
    }

    private fun proxy() {
        val hello = Proxy.newProxyInstance(Hello::class.java.classLoader, arrayOf<Class<*>>(Hello::class.java)) { _, method, args ->
            Log.e("Reflection", "proxy: $method")
            if (method.name == "morning") {
                Log.e("Reflection", "Good Morning: ${args[0]}")
            }
        } as Hello
        hello.morning("WangZhen")
    }

    private fun reflection() {
        val str = "Hello"
        printClassInfo(str.javaClass)
        printClassInfo(Class.forName("java.lang.Runnable"))
        printClassInfo(Array<Int>::class.java)
        val user = User("wangzhen", 28)
        val userClass = user.javaClass
        val nameField = userClass.getDeclaredField("name")
        nameField.isAccessible = true
        val nameValue = nameField.get(user)
        Log.e("Reflection", "nameValue: $nameValue")
        nameField.set(user, "wz")
        Log.e("Reflection", "nameField: ${nameField.get(user)}")

        val toString = userClass.getMethod("toString")
        Log.e("Reflection", "toString Method: $toString")
        Log.e("Reflection", "toString ReturnType: ${toString.returnType}")
        Log.e("Reflection", "toString parameterTypes: ${toString.parameterTypes.contentToString()}")
        Log.e("Reflection", "toString invoke: ${toString.invoke(user)}")

        val ageField = User::class.java.getDeclaredField("age")
        Log.e("Reflection", "field name: $nameField")
        Log.e("Reflection", "field age: $ageField")
        if (userClass.isAnnotationPresent(Report::class.java)) {
            val reportType = userClass.getAnnotation(Report::class.java)
            Log.e("Reflection", "annontation: $reportType")
            Log.e("Reflection", "annontation level: ${reportType?.level}")
            Log.e("Reflection", "annontation type: ${reportType?.type}")
            Log.e("Reflection", "annontation value: ${reportType?.value}")
        }
        val stock = Stock("腾讯控股", "00700", 1)
        check(stock)
    }

    private fun check(stock: Stock) {
        for (field in stock.javaClass.fields) {
            val range = field.getAnnotation(Range::class.java)
            range?.let {
                val value = field.get(stock)
                if (value is String) {
                    if (value.length < range.min || value.length > range.max) {
                        throw IllegalArgumentException("invalid field ${field.name}")
                    }
                }
            }
        }
    }

    private fun <T : Any?> printClassInfo(clazz: Class<T>) {
        Log.e("Reflection", "Class Name: ${clazz.name}")
        Log.e("Reflection", "Simple Name: ${clazz.simpleName}")
        Log.e("Reflection", "Package Name: ${clazz.`package`?.name}")
        Log.e("Reflection", "is interface: ${clazz.isInterface}")
        Log.e("Reflection", "is enum: ${clazz.isEnum}")
        Log.e("Reflection", "is array: ${clazz.isArray}")
        Log.e("Reflection", "is primitive: ${clazz.isPrimitive}")
    }


}