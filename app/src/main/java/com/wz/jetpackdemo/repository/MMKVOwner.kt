package com.wz.jetpackdemo.repository

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val kv: MMKV = MMKV.defaultMMKV()

interface MMKVOwner {
    val kv: MMKV get() = com.wz.jetpackdemo.repository.kv
}

fun MMKVOwner.mmkvInt(default: Int = 0) =
    MMKVProperty(MMKV::decodeInt, MMKV::encode, default)

fun MMKVOwner.mmkvLong(default: Long) =
    MMKVProperty(MMKV::decodeLong, MMKV::encode, default)

fun MMKVOwner.mmkvBool(default: Boolean) =
    MMKVProperty(MMKV::decodeBool, MMKV::encode, default)

fun MMKVOwner.mmkvFloat(default: Float) =
    MMKVProperty(MMKV::decodeFloat, MMKV::encode, default)

fun MMKVOwner.mmkvDouble(default: Double) =
    MMKVProperty(MMKV::decodeDouble, MMKV::encode, default)

fun MMKVOwner.mmkvString() =
    MMKVNullableProperty(MMKV::decodeString,MMKV::encode)

fun MMKVOwner.mmkvString(default: String) =
    MMKVNullablePropertyWithDefault(MMKV::decodeString, MMKV::encode, default)

fun MMKVOwner.mmkvStringSet():ReadWriteProperty<MMKVOwner,Set<String>?> =
    MMKVNullableProperty(MMKV::decodeStringSet,MMKV::encode)

fun MMKVOwner.mmkvStringSet(defaultSet: Set<String>) =
    MMKVNullablePropertyWithDefault(MMKV::decodeStringSet, MMKV::encode, defaultSet)

fun MMKVOwner.mmkvBytes(){
    MMKVNullableProperty(MMKV::decodeBytes,MMKV::encode)
}

fun MMKVOwner.mmkvBytes(default: ByteArray) =
    MMKVNullablePropertyWithDefault(MMKV::decodeBytes, MMKV::encode, default)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable() =
    MMKVParcelableProperty(T::class.java)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable(default: T) =
    MMKVParcelablePropertyWithDefault(T::class.java, default)

class MMKVProperty<V>(
    private val decode: MMKV.(String, V) -> V,
    private val encode: MMKV.(String, V) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decode(property.name, defaultValue)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVNullableProperty<V>(
    private val decode: MMKV.(String, V?) -> V?,
    private val encode: MMKV.(String, V?) -> Boolean
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decode(property.name,null)


    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name,value)
    }

}

class MMKVNullablePropertyWithDefault<V>(
    private val decode: MMKV.(String, V?) -> V?,
    private val encode: MMKV.(String, V?) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decode(property.name, null) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }


}

class MMKVParcelableProperty<V : Parcelable>(private val clazz: Class<V>) :
    ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decodeParcelable(property.name, clazz)


    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }

}

class MMKVParcelablePropertyWithDefault<V : Parcelable>(
    private val clazz: Class<V>,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decodeParcelable(property.name, clazz) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }

}