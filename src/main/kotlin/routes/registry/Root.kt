package com.miaoyongzheng.routes.registry

import io.ktor.server.routing.*

fun Route.registryRoutes(){
    route("/registry"){
        departmentRoutes()
        hospitalRoutes()
    }
}