package com.wz.jetpackdemo.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper(context: Context) : SQLiteOpenHelper(context, "book_provider.db", null, 1) {
    companion object{
        @JvmStatic
        val BOOK_TABLE_NAME = "book"
        @JvmStatic
        val USER_TALBE_NAME = "user"
    }

    // 图书和用户信息表
    private val CREATE_BOOK_TABLE: String? = "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME(_id INTEGER PRIMARY KEY,name TEXT)"
    private val CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TALBE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT," +
                            "sex INT)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_BOOK_TABLE)
        db?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //TODO ignored

    }

}