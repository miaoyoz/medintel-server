package com.miaoyongzheng.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

@Serializable
data class DepartmentCategory(
    val id: Int,
    val name: String,
    val detailCategory: String
)

class DepartmentCategoryService: BaseSchema(DepartmentCategories) {

    object DepartmentCategories : Table("department_categories") {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)
        val detailCategory = varchar("detail_category", 255)

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun getAllCategories(): List<DepartmentCategory> = dbQuery {
        DepartmentCategories.selectAll().map {
            DepartmentCategory(
                id = it[DepartmentCategories.id],
                name = it[DepartmentCategories.name],
                detailCategory = it[DepartmentCategories.detailCategory]
            )
        }
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        DepartmentCategories.selectAll()
            .where { DepartmentCategories.detailCategory eq name }
            .map { it[DepartmentCategories.id] }
            .singleOrNull()
    }
}