package com.wz.jetpackdemo.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.wz.jetpackdemo.database.DbOpenHelper
import android.database.sqlite.SQLiteDatabase
import com.tencent.mmkv.MMKV


class BookProvider : ContentProvider() {
    val TAG = BookProvider::class.java.simpleName
    lateinit var mDb: SQLiteDatabase
    init {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE)
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE)

    }

    companion object {
        @JvmStatic
        val AUTHORITY = "com.wz.jetpackdemo.provider"

        @JvmStatic
        val BOOK_CONTENT_URI = Uri.parse("content://$AUTHORITY/book")

        @JvmStatic
        val USER_CONTENT_URI = Uri.parse("content://$AUTHORITY/user")

        @JvmStatic
        val BOOK_URI_CODE = 0

        @JvmStatic
        val USER_URI_CODE = 1

        @JvmStatic
        val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    }

    override fun onCreate(): Boolean {
        Log.e(TAG, "onCreate,current thread: ${Thread.currentThread().name}")
        initProviderData()
        MMKV.initialize(context)
        return true
    }

    private fun initProviderData() {
        mDb = DbOpenHelper(context!!).writableDatabase
        mDb.execSQL("delete from ${DbOpenHelper.BOOK_TABLE_NAME}")
        mDb.execSQL("delete from ${DbOpenHelper.USER_TALBE_NAME}")
        mDb.execSQL("insert into book values(3,'Android');")
        mDb.execSQL("insert into book values(4,'ios');")
        mDb.execSQL("insert into book values(5,'H5');")
        mDb.execSQL("insert into user values(1,'wangzhen',1);")
        mDb.execSQL("insert into user values(2,'wz',1);")
    }

    private fun getTableName(uri: Uri): String {
        when (sUriMatcher.match(uri)) {
            BOOK_URI_CODE -> return DbOpenHelper.BOOK_TABLE_NAME
            USER_URI_CODE -> return DbOpenHelper.USER_TALBE_NAME
            else -> return ""
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.e(TAG, "query,current thread: ${Thread.currentThread().name}")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI: $uri")
        }
        return mDb.query(tableName,projection,selection,selectionArgs,null,null,sortOrder,null)
    }

    override fun getType(uri: Uri): String? {
        Log.e(TAG, "getType")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e(TAG, "insert")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI: $uri")
        }
        mDb.insert(tableName, null, values)
        return uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.e(TAG, "delete")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI: $uri")
        }
        val count = mDb.delete(tableName, selection, selectionArgs)
        if (count > 0) {
            context?.contentResolver?.notifyChange(uri, null)
        }
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.e(TAG, "update")
        val tableName = getTableName(uri)
        if (tableName.isEmpty()) {
            throw IllegalArgumentException("Unsupported URI: $uri")
        }
        val row = mDb.update(tableName, values, selection, selectionArgs)
        if (row > 0) {
            context?.contentResolver?.notifyChange(uri,null)
        }
        return row
    }
}