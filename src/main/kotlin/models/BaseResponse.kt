package com.miaoyongzheng.models

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


data class BaseResponse<out T>(
    val code: Int,
    val message: String,
    val data: T? = null
)

suspend fun <T> RoutingContext.response(code: Int, message: String, data: T? = null) {
    call.respond(
        BaseResponse(code, message, data)
    )
}

suspend fun <T> RoutingContext.success(data: T? = null, message: String? = null) {
    call.respond(
        BaseResponse(HttpStatusCode.OK.value, message ?: HttpStatusCode.OK.description, data)
    )
}

suspend fun <T> RoutingContext.error(message: String? = null, data: T? = null) {
    call.respond(
        BaseResponse(HttpStatusCode.BadRequest.value, message ?: HttpStatusCode.BadRequest.description, data)
    )
}

suspend fun RoutingContext.errorNull(message: String? = null) {
    call.respond(
        BaseResponse(HttpStatusCode.BadRequest.value, message ?: HttpStatusCode.BadRequest.description, null)
    )
}