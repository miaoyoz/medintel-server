package com.miaoyongzheng.utils

class Const {
    companion object {

        //JWT
        const val USER_UNAUTHORIZED: String = "User Unauthorized"
        const val CLAIM_USER_ID = "userId"


        //User
        const val USER_CREATED_SUCCESSFULLY = "User created successfully"
        const val USER_S_PHONE_NUMBER_ALREADY_EXISTS = "User's phone number already exists"
        const val USER_LOGIN_SUCCESSFULLY = "User login successfully"
        const val USER_NOT_LOGIN = "User not login"
        const val USER_NOT_FOUND = "User not found"
        const val USER_AVATAR_UPLOAD_SUCCESSFULLY = "User avatar upload successfully"
        const val USER_AVATAR_UPLOAD_FAILED = "User avatar upload failed"
        const val USER_DEFAULT_AVATAR = "https://medintel.oss-cn-shanghai.aliyuncs.com/medintel_android_avatar_default.png"

        //Registry
        const val NO_RELEVANT_HOSPITALS_WERE_FOUND= "No relevant hospitals were found"
    }
}
