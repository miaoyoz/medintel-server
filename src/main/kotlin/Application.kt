package com.miaoyongzheng

import com.miaoyongzheng.configs.*
import com.miaoyongzheng.sample.configureFrameworks
import io.ktor.server.application.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureRedis()
    configureSerialization()
    configureHTTP()
    configureWebSocket()
    configureDatabases()
    configureSecurity()
    configureFrameworks()
    configureRouting()
    environment.monitor.subscribe(ApplicationStopping) {
        Redis.jedis.close()
    }
}
