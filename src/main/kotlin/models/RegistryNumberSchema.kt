package com.miaoyongzheng.models

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table

class RegistryNumberService:BaseSchema(RegistryNumbers) {
    object RegistryNumbers : Table(name = "registry_numbers") {
        val id = integer("id").autoIncrement()
        val hospital_id = integer("hospital_id")
        val department_id = integer("department_id")
        val doctor_id = integer("doctor_id")
        val registry_date = varchar("registry_date", length = 255)
        val quantity = integer("quantity")
        val price = float("price")

        override val primaryKey = PrimaryKey(id)
    }
}