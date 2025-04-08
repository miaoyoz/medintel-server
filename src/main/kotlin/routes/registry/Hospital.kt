package com.miaoyongzheng.routes.registry

import com.miaoyongzheng.configs.Redis
import com.miaoyongzheng.models.*
import com.miaoyongzheng.utils.Const
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal fun Route.hospitalRoutes() {

    val departmentService = DepartmentCategoryService()
    val hospitalDepartmentService = HospitalDepartmentService()
    val hospitalService = HospitalService()
    val hospitalTagService = HospitalTagService()

    route("/hospital") {

        //根据科室查找医院
        get("/department") {
            val parameters = call.parameters
            //获取当前页数和每页大小
            val page = parameters["page"]?.toInt()
            val pageSize = parameters["pageSize"]?.toInt()
            if (page == null || pageSize == null) {
                errorNull()
                return@get
            }
            parameters["name"]?.let { departmentName ->
                //现根据departmentName查询departmentId
                departmentService.getIdByName(departmentName)?.let { departmentId ->
                    //根据departmentId查询所有医院id
                    hospitalDepartmentService.getHospitalIdsByDepartmentIdWithPage(departmentId, page, pageSize)
                        .let { hospitalIds ->
                            //如果为空列表就返回提示信息
                            if (hospitalIds.isEmpty()) {
                                success(emptyList<String>(), Const.NO_RELEVANT_HOSPITALS_WERE_FOUND)
                            } else {
                                //根据所有的医院id查询所有的医院信息
                                val hospitals = emptyList<Map<String, Any>>().toMutableList()
                                val total = hospitalIds.size
                                hospitalIds.forEach { id ->
                                    //获取医院信息
                                    val hospital = hospitalService.getHospitalById(id)
                                    //获取医院tags
                                    val tags = hospitalTagService.getTagsByHospitalId(id)
                                    if (hospital != null) {
                                        hospitals.add(
                                            mapOf(
                                                "id" to hospital.id,
                                                "image" to hospital.icon,
                                                "name" to hospital.name,
                                                "distance" to "${(Math.random() * 1000).toInt()}m",
                                                "introduction" to hospital.introduction,
                                                "location" to hospital.address,
                                                "tags" to tags,
                                            )
                                        )
                                    }
                                }
                                //返回数据
                                success(mapOf(
                                    "hospitals" to hospitals,
                                    "total" to total,
                                    "page" to page,
                                    "pageSize" to pageSize
                                ))
                            }
                        }
                } ?: success(emptyList<String>(), Const.NO_RELEVANT_HOSPITALS_WERE_FOUND)
            } ?: errorNull()
        }

        //获取所有医院
        //状态码201表示没有数据，200表示有数据，400表示参数错误
        get("/all") {
            call.parameters["page"]?.toInt()?.let { page ->
                call.parameters["pageSize"]?.toInt()?.let { pageSize ->
                    //从redis获取所有的医院数量
                    val total = withContext(Dispatchers.IO) {
//                        Redis.jedis.get("hospital_count")?.toLong() ?:
                        //如果redis中没有数据就从数据库中获取
                        hospitalService.getHospitalCount().apply {
                            //将数据存入redis
//                            Redis.jedis.set("hospital_count", toString())
                        }
                    }
                    if (total == 0L) {
                        response(201, "没有数据", null)
                    } else {
                        //获取所有的医院信息
                        val hospitals = hospitalService.getAllHospitalsWithPage(page, pageSize)

                        if (hospitals.isEmpty()) {
                            response(201, "没有数据", null)
                        } else {
                            //最终的hospital
                            val finalHospitals = emptyList<Map<String, Any>>().toMutableList()

                            hospitals.forEach { hospital ->
                                //获取医院tags
                                val tags = hospitalTagService.getTagsByHospitalId(hospital.id)
                                finalHospitals.add(
                                    mapOf(
                                        "id" to hospital.id,
                                        "image" to hospital.icon,
                                        "name" to hospital.name,
                                        "introduction" to hospital.introduction,
                                        "distance" to "${(Math.random() * 1000).toInt()}m",
                                        "location" to hospital.address,
                                        "tags" to tags
                                    )
                                )
                            }
                            response(
                                200,
                                "获取成功",
                                mapOf(
                                    "hospitals" to finalHospitals,
                                    "total" to total,
                                    "page" to page,
                                    "pageSize" to pageSize
                                )
                            )
                        }
                    }

                } ?: errorNull()
            } ?: errorNull()
        }

    }
}