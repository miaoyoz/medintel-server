package com.miaoyongzheng.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserHospitalHistoryService : BaseSchema(UserHospitalHistory) {
    object UserHospitalHistory : Table("user_hospital_histories") {
        val id = integer("id").autoIncrement()
        val userId = integer("user_id")
        val hospitalId = integer("hospital_id")

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun getUserHospitalHistoryByUserId(userId: Int): List<Int> = dbQuery {
        UserHospitalHistory.selectAll()
            .where { UserHospitalHistory.userId eq userId }
            .map {
                it[UserHospitalHistory.hospitalId]
            }
    }

    suspend fun addUserHospitalHistory(userId: Int, hospitalId: Int) = dbQuery {
        UserHospitalHistory.insert {
            it[UserHospitalHistory.userId] = userId
            it[UserHospitalHistory.hospitalId] = hospitalId
        }
    }

    suspend fun haveHistory(userId: Int, hospitalId: Int) = dbQuery {
        UserHospitalHistory.selectAll()
           .where { UserHospitalHistory.userId eq userId and  (UserHospitalHistory.hospitalId eq hospitalId) }
           .map {
                it[UserHospitalHistory.hospitalId]
            }.isEmpty()//如果为空则没有历史记录
    }
}