package com.miaoyongzheng.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

class HospitalDepartmentService : BaseSchema(HospitalDepartments) {

    object HospitalDepartments : Table(name = "hospital_departments") {
        val id = integer("id").autoIncrement()
        val hospital_id = integer("hospital_id")
        val department_id = integer("department_id")

        override val primaryKey = PrimaryKey(id)
    }

    /**
     * 按医院 ID 获取部门 ID
     * @param [hospitalId] 医院 ID
     * @return [List<Int>] 返回的是一个整数列表，其中包含了与给定医院 ID 相关的所有部门 ID。如果没有找到任何相关的部门 ID，
     * 则返回一个空列表。
     */
    suspend fun getDepartmentIdsByHospitalId(hospitalId: Int): List<Int> = dbQuery {
        HospitalDepartments.selectAll()
            .where { HospitalDepartments.hospital_id eq hospitalId }
            .map { it[HospitalDepartments.department_id] }

    }

    /**
     * 按部门 ID 获取医院 ID
     * @param [hospitalId] 医院 ID
     * @return [List<Int>] 返回的是一个整数列表，其中包含了与给定部门 ID 相关的所有医院 ID。如果没有找到任何相关的医院 ID，
     * 则返回一个空列表。
     */
    suspend fun getHospitalIdsByDepartmentId(hospitalId: Int): List<Int> = dbQuery {
        HospitalDepartments.selectAll()
            .where { HospitalDepartments.department_id eq hospitalId }
            .map { it[HospitalDepartments.hospital_id] }
    }


    /**
     * 使用页面按部门 ID 获取医院 ID
     * @param [hospitalId] 医院 ID
     * @param [page] 页
     * @param [pageSize] 页面大小
     * @return [List<Int>] 返回的是一个整数列表，其中包含了与给定部门 ID 相关的所有医院 ID。如果没有找到任何相关的医院 ID，
     * 则返回一个空列表，实现了分页的功能
     */
    suspend fun getHospitalIdsByDepartmentIdWithPage(hospitalId: Int, page: Int = 0, pageSize: Int = 10): List<Int> =
        dbQuery {
            HospitalDepartments.selectAll()
                .where { HospitalDepartments.department_id eq hospitalId }
                .limit(pageSize)
                .offset(page * pageSize.toLong())
                .map { it[HospitalDepartments.hospital_id] }
        }

}