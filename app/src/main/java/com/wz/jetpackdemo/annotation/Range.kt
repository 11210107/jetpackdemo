package com.wz.jetpackdemo.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Range(val min: Int = 0, val max: Int = 255)
