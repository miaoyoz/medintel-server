package com.miaoyongzheng.routes.registry

import com.miaoyongzheng.models.BaseResponse
import com.miaoyongzheng.models.DepartmentCategoryService
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.departmentRoutes() {
    route("/department") {

        val departmentCategoryService = DepartmentCategoryService()
//        val departmentCategoryService by inject<DepartmentCategoryService>()
        //获取挂号科室分类的所有科室列表
        get("/categoryList") {

            val list = departmentCategoryService.getAllCategories()
                .groupBy { it.name }
                .mapValues { entry ->
                    entry.value.map { it.detailCategory }
                }.toList()

            call.respond(BaseResponse(200, "success", list))
        }
    }
}