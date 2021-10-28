package com.wz.jetpackdemo.model

import com.wz.jetpackdemo.annotation.Range
import com.wz.jetpackdemo.annotation.Report
import java.io.Serializable

@Report(value = "User",type = 1,level = "UserLevel")
class User(@Range(min = 1,max = 20)var name: String, @Range(min = 1,max = 3) var age: Int, var id: Int = -1):Serializable{
    private val serialVersionUID = 519067123721295773L
    override fun toString(): String {
        return "User{ age=$age, name=$name,id=$id}"
    }
}