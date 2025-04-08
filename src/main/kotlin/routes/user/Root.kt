package com.miaoyongzheng.routes.user

import com.miaoyongzheng.models.*
import com.miaoyongzheng.utils.AuthUtil
import com.miaoyongzheng.utils.Const
import com.miaoyongzheng.utils.OSSUtil
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.jvm.javaio.*

fun Route.userRoutes() {
//    val userService by inject<UserService>()
    val userService = UserService()

    route("/user") {
        hospitalRoutes()
        // 注册用户
        post("/register") {
            val user = call.receive<User>()

            println(user)
            //检查手机号是否已存在
            val phone = user.phone
            userService.getUserByPhone(phone)?.let {
                success(null, Const.USER_S_PHONE_NUMBER_ALREADY_EXISTS)
                return@post
            }
            // 手机号不存在就创建用户
            userService.create(user)
            response(HttpStatusCode.Created.value, Const.USER_CREATED_SUCCESSFULLY, null)
        }
        // 登录用户
        post("/login") {
            // 处理登录逻辑r
            call.receive<User>().let { user ->
                println("user login info: $user")
                //根据手机号密码验证是否存在
                userService.getUserByPhonePassword(phone = user.phone, password = user.password)?.let {
                    //如果存在就返回用户包含id的token(状态码200)
                    val token = AuthUtil.generateJWTToken(environment, it.id)
                    println("user login token: $token\nusername: ${it.username}\navatar: ${it.avatar}")
                    success(
                        message = Const.USER_LOGIN_SUCCESSFULLY, data = mapOf(
                            "token" to token,
                            "username" to it.username,
                            "avatar" to it.avatar
                        )
                    )
                    //如果不存在就返回错误信息（状态码400）
                } ?: response(HttpStatusCode.BadRequest.value, Const.USER_NOT_FOUND, null)
            }
        }

        // 上传用户头像
        post("/uploadAvatar") {
            val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)
            multipartData.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        //前端已经判断是否为图片格式
                        part.provider().toInputStream().use { input ->
                            //上传阿里云OSS
                            OSSUtil.uploadFileToOSS(input)?.let { url ->
                                //不为null则上传成功
                                call.respond(
                                    HttpStatusCode.OK, BaseResponse(
                                        HttpStatusCode.OK.value, Const.USER_AVATAR_UPLOAD_SUCCESSFULLY,
                                        mapOf("url" to url)
                                    )
                                )
                                //为null上传失败
                            } ?: call.respond(
                                HttpStatusCode.InternalServerError, BaseResponse(
                                    HttpStatusCode.InternalServerError.value, Const.USER_AVATAR_UPLOAD_FAILED, null
                                )
                            )
                        }
                    }

                    else -> {
                        call.respond(
                            HttpStatusCode.BadRequest, BaseResponse(
                                HttpStatusCode.BadRequest.value, "Invalid part type", null
                            )
                        )
                    }
                }
                part.dispose()
            }
        }
    }
}