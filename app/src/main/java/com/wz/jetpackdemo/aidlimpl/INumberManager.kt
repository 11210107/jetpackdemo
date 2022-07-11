package com.wz.jetpackdemo.aidlimpl

import android.os.IInterface
import android.os.RemoteException
import com.wz.jetpackdemo.model.Book

interface INumberManager : IInterface {
     @Throws(RemoteException::class)
     fun getNumberList(): List<Book>

     @Throws(RemoteException::class)
     fun addNumber(book: Book?)

}
