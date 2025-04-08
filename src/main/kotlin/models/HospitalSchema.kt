package com.miaoyongzheng.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

@Serializable
data class Hospital(
    val id: Int,
    val name: String,
    val icon: String,
    val address: String,
    val phone: String,
    val openTime: String,
    val introduction: String,
    val info: String
)

class HospitalService : BaseSchema(Hospitals) {
    object Hospitals : Table("hospitals") {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)
        val icon = varchar("icon", 1024)
        val address = varchar("address", 255)
        val phone = varchar("phone", 32)
        val openTime = varchar("open_time", 255)
        val introduction = varchar("introduction", 1024)
        val info = varchar("info", 2048)
        override val primaryKey = PrimaryKey(id)
    }

    suspend fun getHospitalById(id: Int) = dbQuery {
        Hospitals.selectAll()
            .where { Hospitals.id eq id }
            .map {
                Hospital(
                    id = it[Hospitals.id],
                    name = it[Hospitals.name],
                    icon = it[Hospitals.icon],
                    address = it[Hospitals.address],
                    phone = it[Hospitals.phone],
                    openTime = it[Hospitals.openTime],
                    introduction = it[Hospitals.introduction],
                    info = it[Hospitals.info]
                )
            }
            .singleOrNull()
    }

    suspend fun getAllHospitalsWithPage(page: Int, pageSize: Int) = dbQuery {
        Hospitals.selectAll()
            .limit(pageSize)
            .offset((page * pageSize).toLong())
            .map {
                Hospital(
                    id = it[Hospitals.id],
                    name = it[Hospitals.name],
                    icon = it[Hospitals.icon],
                    address = it[Hospitals.address],
                    phone = it[Hospitals.phone],
                    openTime = it[Hospitals.openTime],
                    introduction = it[Hospitals.introduction],
                    info = it[Hospitals.info]
                )
            }

    }

    suspend fun getHospitalCount() = dbQuery {
        Hospitals.selectAll().count()
    }

}