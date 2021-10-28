// IBookManager.aidl
package com.wz.jetpackdemo;

// Declare any non-default types here with import statements
import com.wz.jetpackdemo.model.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}