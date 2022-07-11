package com.wz.jetpackdemo.aidlimpl

import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import com.wz.jetpackdemo.model.Book

abstract class NumblerManagerImpl : Binder(), INumberManager {

    init {
        attachInterface(this, DESCRIPTOR)
    }

    companion object {
        private val DESCRIPTOR = "com.wz.jetpackdemo.aidlimpl.NumblerManagerImpl"
        val TRANSACTION_getNumberList = IBinder.FIRST_CALL_TRANSACTION + 0
        val TRANSACTION_addNumber = IBinder.FIRST_CALL_TRANSACTION + 1
        @JvmStatic
        fun asInterface(binder: Binder):INumberManager? {
            if (binder == null) {
                return null
            }
            val iin = binder.queryLocalInterface(DESCRIPTOR)
            if ((iin != null) && iin is INumberManager) {
                return iin
            }
            return Proxy(binder)
        }
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            INTERFACE_TRANSACTION -> {
                reply?.writeString(DESCRIPTOR)
                return true
            }
            TRANSACTION_getNumberList ->{
                data.enforceInterface(DESCRIPTOR)
                val result = getNumberList()
                reply?.writeNoException()
                reply?.writeTypedList(result)
                return true
            }
            TRANSACTION_addNumber ->{
                data.enforceInterface(DESCRIPTOR)
                var arg0 = if (0 != data.readInt()) {
                    Book.createFromParcel(data)
                } else {
                    null
                }
                this.addNumber(arg0)
                reply?.writeNoException()
                return true
            }
        }
        return super.onTransact(code, data, reply, flags)
    }
    private class Proxy internal constructor(val remote: Binder): INumberManager {
        override fun getNumberList(): List<Book> {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()
            var result: List<Book>
            try {
                data.writeInterfaceToken(DESCRIPTOR)
                remote.transact(TRANSACTION_getNumberList, data, reply, 0)
                reply.readException()
                result = reply.createTypedArrayList(Book.CREATOR)!!
            } finally {
                data.recycle()
                reply.recycle()
            }
            return result

        }

        override fun addNumber(book: Book?) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()
            try {
                data.writeInterfaceToken(DESCRIPTOR)
                if (book != null) {
                    data.writeInt(1)
                    book.writeToParcel(data, 0)
                } else {
                    data.writeInt(0)
                }
                remote.transact(TRANSACTION_addNumber, data, reply, 0)
                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        override fun asBinder(): IBinder {
            return remote
        }


    }


    override fun asBinder(): IBinder {
        return this
    }
}