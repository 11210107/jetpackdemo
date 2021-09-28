package com.wz.jetpackdemo.model

class User(var name: String, var age: Int,var id: Int = -1){
    override fun toString(): String {
        return "User{ age=$age, name=$name,id=$id}"
    }

}