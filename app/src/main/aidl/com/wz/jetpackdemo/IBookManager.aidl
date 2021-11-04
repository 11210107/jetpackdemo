// IBookManager.aidl
package com.wz.jetpackdemo;

// Declare any non-default types here with import statements
import com.wz.jetpackdemo.model.Book;
import com.wz.jetpackdemo.model.INewBookArraivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(INewBookArraivedListener listener);
    void unregisterListener(INewBookArraivedListener listener);

}