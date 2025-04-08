package com.miaoyongzheng.models

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class HospitalTagService:BaseSchema(HospitalTags) {
    object HospitalTags : Table(name = "hospital_tags") {
        val id = integer("id").autoIncrement()
        val hospital_id = integer("hospital_id")
        val tag = varchar("tag", length = 255)

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun getTagsByHospitalId(id:Int) = dbQuery {
        HospitalTags.selectAll()
            .where{ HospitalTags.hospital_id eq id}
            .map { it[HospitalTags.tag] }
    }

}