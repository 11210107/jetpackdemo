package com.wz.jetpackdemo.model

import android.os.Parcel
import android.os.Parcelable

class Book(var bookId:Int, var bookName: String?):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookId)
        parcel.writeString(bookName)
    }

    /**
     * 参数是一个Parcel,用它来存储与传输数据
     * @param dest
     */
    fun readFromParcel(dest: Parcel) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        bookName = dest.readString()
        bookId = dest.readInt()
    }
    override fun describeContents(): Int {
        return 0
    }


    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Book(bookId=$bookId, bookName=$bookName) \n"
    }

}