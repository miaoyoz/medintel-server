package com.miaoyongzheng.models

import org.jetbrains.exposed.sql.Table

class DoctorService:BaseSchema(Doctors){

    object Doctors:Table(name = "doctors"){
        val id = integer("id").autoIncrement()
        val name = varchar("name",255)
        val avatar = varchar("avatar",1024)
        val departmentId = integer("department_id")
        val score = float("score")
        val specialty = varchar("specialty",1024)
        val introduction = varchar("introduction",1024)
        val hospitalId = integer("hospital_id")
        override val primaryKey = PrimaryKey(id)
    }


}