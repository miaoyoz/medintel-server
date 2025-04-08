package com.miaoyongzheng.utils

import kotlin.random.Random

class UserUtil {
    companion object {
        //生成随机length长度的用户名
        //示例：generateRandomUsername(10) -> "aBcDeFgHiJ"
        fun generateRandomUsername(length: Int): String {
            val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // 字母和数字池
            return (1..length)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        }
    }
}