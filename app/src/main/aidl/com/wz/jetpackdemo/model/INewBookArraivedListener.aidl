// INewBookArraivedListener.aidl
package com.wz.jetpackdemo.model;

import com.wz.jetpackdemo.model.Book;

interface INewBookArraivedListener {

    void onNewBookArrived(in Book newBook);
}