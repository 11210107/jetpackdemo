package com.wz.jetpackdemo.extension

class RealSubject:Subject by Delegate()
//{
//    private val delegate: Delegate = Delegate()
//    override fun buy() {
//        delegate.buy()
//    }
//}