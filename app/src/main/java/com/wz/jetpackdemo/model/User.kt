package com.wz.jetpackdemo.model

class User(var name: String, var age: Int){
    override fun toString(): String {
        return "User{ age=$age, name=$name}"
    }

}