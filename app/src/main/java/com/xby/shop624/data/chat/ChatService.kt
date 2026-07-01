package com.xby.shop624.data.chat

import android.util.Log

object ChatService {

    private const val TAG = "ChatService"

    fun init() {
        Log.d(TAG, "客服聊天服务初始化完成")
    }

    fun getKefuId(): String = "kefu"
}