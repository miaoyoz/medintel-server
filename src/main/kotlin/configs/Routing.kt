package com.miaoyongzheng.configs

import com.miaoyongzheng.models.BaseResponse
import com.miaoyongzheng.models.UserService
import com.miaoyongzheng.routes.registry.departmentRoutes
import com.miaoyongzheng.routes.registry.hospitalRoutes
import com.miaoyongzheng.routes.registry.registryRoutes
import com.miaoyongzheng.routes.user.userRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureRouting() {
    install(DoubleReceive)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError, BaseResponse(
                    HttpStatusCode.InternalServerError.value, cause.message ?: "", null
                )
            )
        }
    }
    install(SSE)


    routing {
        get("/test") {
            call.respondText("Hello World!")
        }
        post("/test"){
            newSuspendedTransaction(Dispatchers.IO){
                newSuspendedTransaction (Dispatchers.IO){
                    println("transaction 1")
                    UserService.Users.insert {
                        it[UserService.Users.username] = "miaoyongzheng"
                        it[UserService.Users.phone] = "123456789003"
                        it[UserService.Users.password] = "123456"
                    }
                }
                newSuspendedTransaction (Dispatchers.IO){
                    println("transaction 2")
                    UserService.Users.insert {
                        it[UserService.Users.username] = "miaoyongzheng"
                        it[UserService.Users.phone] = "123456789004"
                        it[UserService.Users.password] = "123456"
                    }
                }
            }

            call.respondText("Hello World!")
            println("transaction finish")
        }

        userRoutes()
        registryRoutes()

    }
}



