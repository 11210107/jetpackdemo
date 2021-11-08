package com.wz.jetpackdemo.aidlimpl

import com.wz.jetpackdemo.ISecurityCenter

class SecurityCenterImpl:ISecurityCenter.Stub() {
    val SECRET_CODE = '^'

    override fun encrypt(content: String?): String {
        val chars = content?.toCharArray()
        chars?.forEachIndexed{i,c ->
            chars[i] = (c.code xor SECRET_CODE.code).toChar()
        }
        return String(chars!!)
    }

    override fun decrypt(password: String?): String {
        return encrypt(password)
    }
}