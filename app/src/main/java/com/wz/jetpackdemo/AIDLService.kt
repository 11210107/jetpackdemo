package com.wz.jetpackdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.wz.jetpackdemo.extension.lock
import com.wz.jetpackdemo.extension.rlock
import com.wz.jetpackdemo.model.Book

class AIDLService:Service() {
    val TAG = AIDLService::class.java.simpleName
    private val books = mutableListOf<Book>()

    override fun onCreate() {
        super.onCreate()
        val androidDevelop = Book(0, "Android开发艺术探索")
        books.add(androidDevelop)
    }
    val mIBookManager = object :IBookManager.Stub(){
        override fun getBookList(): MutableList<Book> {
            Log.e(TAG,"invoking getBookList() method,now the list is:${books}")
            return rlock {
                books
            }
        }

        override fun addBook(book: Book?) {
            lock {
                if (books == null) {
                    books == mutableListOf<Book>()
                }
                if (book == null) {
                    Log.e(TAG, "book is null in In")
                }
                if (!books.contains(book)) {
                    books.add(book!!)
                }
                Log.e(TAG,"invoking addBook() method , now the list is : $books")
            }
        }

    }


    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind intent = ${intent.toString()}")
        return mIBookManager
    }
}