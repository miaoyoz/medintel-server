package com.miaoyongzheng.routes.user

import com.miaoyongzheng.models.*
import com.miaoyongzheng.utils.AuthUtil
import com.miaoyongzheng.utils.Const
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.hospitalRoutes() {

    val userHospitalHistoryService = UserHospitalHistoryService()
    val hospitalService = HospitalService()

    route("/hospital") {
        authenticate("auth-jwt") {
            get("/history") {
                AuthUtil.getJWTUserId(call)?.let {
                    //根据id找到用户的所有历史医院id
                    val hospitalIds = userHospitalHistoryService.getUserHospitalHistoryByUserId(it)
                    if (hospitalIds.isEmpty()) {
                        response(200, "没有历史记录", null)
                    } else {
                        //根据id找到所有的医院信息
                        val hospitals = hospitalIds.map { hospitalId ->
                            hospitalService.getHospitalById(hospitalId)
                        }
                        //返回数据
                        response(200, "获取成功", mapOf("hospitals" to hospitals))
                    }
                } ?: errorNull(Const.USER_NOT_LOGIN)
            }

            post("/addHistory") {
                //获取医院id
                call.pathParameters["hospitalId"]?.let { hospitalId ->
                    //获取医院id
                    AuthUtil.getJWTUserId(call)?.let { userId ->
                        //判断是否已经存在
                        if (userHospitalHistoryService.haveHistory(userId, hospitalId.toInt())) {
                            //如果存在不添加
                            response(200, "已经存在", null)
                        } else {
                            //不存在则添加
                            userHospitalHistoryService.addUserHospitalHistory(userId, hospitalId.toInt())
                            response(201, "添加成功", null)
                        }

                    } ?: errorNull(Const.USER_NOT_LOGIN)
                } ?: errorNull()

            }
        }
    }
}