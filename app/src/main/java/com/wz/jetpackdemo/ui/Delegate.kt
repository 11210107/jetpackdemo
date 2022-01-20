package com.wz.jetpackdemo.ui

import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Delegate:ReadWriteProperty<Any?,String> {
    val TAG = Delegate::class.java.simpleName
    override fun getValue(thisRef: Any?, property: KProperty<*>): String =
        "$thisRef,thank you for delegating '${property.name}' to me!"


    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        Log.e(TAG, "$value has been assigned to '${property.name}' in $thisRef")
    }
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
//        "thisRef,thank you for delegating '${property.name}' to me!"
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        Log.e(TAG, "$value has been assigned to '${property.name}' in $thisRef")
//    }
}