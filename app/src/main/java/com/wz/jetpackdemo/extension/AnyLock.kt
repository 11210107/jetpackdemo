package com.wz.jetpackdemo.extension

/**
 * 给Any添加锁同步的扩展函数
 * */
fun Any.lock(body: ()->Unit) {
    synchronized(this) {
        body()
    }
}
fun <T>Any.rlock(body: ()-> T): T{
    synchronized(this) {
        return body()
    }
}