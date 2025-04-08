package com.miaoyongzheng.sample

import com.miaoyongzheng.models.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureFrameworks() {
    install(Koin) {
        slf4jLogger()
        modules(module {
//            single <UserService>{
//                UserService()
//            }
//            single <HospitalService>{
//                HospitalService()
//            }
//            single <HospitalTagService>{
//                HospitalTagService()
//            }
//            single <DepartmentCategoryService>{
//                DepartmentCategoryService()
//            }
//            single <HospitalDepartmentService>{
//                HospitalDepartmentService()
//            }
//            single <DoctorService>{
//                DoctorService()
//            }
//            single <RegistryNumberService>{
//                RegistryNumberService()
//            }
        })
    }
}
