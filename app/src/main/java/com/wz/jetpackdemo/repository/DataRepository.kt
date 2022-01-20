package com.wz.jetpackdemo.repository

import com.tencent.mmkv.MMKV

object DataRepository:MMKVOwner {
    override val kv: MMKV
        get() = MMKV.mmkvWithID("DataRepository")
    var isFirstLaunch: Boolean by mmkvBool(default = true)

}