package com.wz.jetpackdemo.repository

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MMKVDelegate:ReadWriteProperty<Any?,Boolean> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
        return true
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {

    }
}