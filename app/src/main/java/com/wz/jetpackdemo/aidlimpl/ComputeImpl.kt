package com.wz.jetpackdemo.aidlimpl

import com.wz.jetpackdemo.ICompute

class ComputeImpl:ICompute.Stub() {
    override fun add(a: Int, b: Int): Int {
        return a + b
    }
}