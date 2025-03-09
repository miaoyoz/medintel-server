package com.miaoyongzheng

import io.ktor.server.application.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureSockets()
//    configureAdministration()
    
    configureFrameworks()
    configureRouting()
}
