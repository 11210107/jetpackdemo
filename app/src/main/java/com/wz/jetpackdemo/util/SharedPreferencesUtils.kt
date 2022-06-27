package com.wz.jetpackdemo.util

import android.content.Context
import android.content.SharedPreferences
import com.wz.jetpackdemo.JetpackApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object SharedPreferencesUtils {
    object User : Delegates() {
        override fun getSharedPreferencesName(): String? {
            return User::class.java.simpleName
        }
        var age by int()
        var name by string()

    }

    abstract class Delegates {
        private val preferences: SharedPreferences by lazy {
            JetpackApplication.context.getSharedPreferences(
                getSharedPreferencesName(),
                Context.MODE_PRIVATE
            )
        }
        abstract fun getSharedPreferencesName(): String?

        fun int(defValue:Int = 0)= object : ReadWriteProperty<Any, Int> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Int {
                return preferences.getInt(property.name, defValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
                preferences.edit().putInt(property.name, value).apply()
            }
        }

        fun string(defValue:String? = null)= object : ReadWriteProperty<Any, String?> {
            override fun getValue(thisRef: Any, property: KProperty<*>): String? {
                return preferences.getString(property.name, defValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
                preferences.edit().putString(property.name, value).apply()
            }
        }

        fun long(defValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Long {
                return preferences.getLong(property.name,defValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
                preferences.edit().putLong(property.name, value).apply()
            }
        }

        fun boolean(defValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
                return preferences.getBoolean(property.name,defValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
                preferences.edit().putBoolean(property.name, value).apply()
            }

        }

        fun float(defValue: Float = 0f) = object : ReadWriteProperty<Any, Float> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Float {
                return preferences.getFloat(property.name, defValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
                preferences.edit().putFloat(property.name, value).apply()
            }

        }
        fun setString(defValue: Set<String>? = null) =
            object : ReadWriteProperty<SharedPreferencesUtils, Set<String>?> {
                override fun getValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>
                ): Set<String>? {
                    return preferences.getStringSet(property.name, defValue)
                }

                override fun setValue(
                    thisRef: SharedPreferencesUtils,
                    property: KProperty<*>,
                    value: Set<String>?
                ) {
                    preferences.edit().putStringSet(property.name, value).apply()
                }
            }

        fun clearAll() {
            preferences.edit().clear().apply()
        }
    }
}