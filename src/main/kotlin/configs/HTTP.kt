package com.miaoyongzheng.configs

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        anyHost()
        anyMethod()
//        allowHeader(HttpHeaders.ContentType)
    }
}