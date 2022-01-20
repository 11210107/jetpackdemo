package com.wz.jetpackdemo.repository

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MMKVBooleanProperty : ReadWriteProperty<MMKVOwner, Boolean> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): Boolean {
        return thisRef.kv.decodeBool(property.name)
    }

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: Boolean) {
        thisRef.kv.encode(property.name, value)
    }

}