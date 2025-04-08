package com.miaoyongzheng.models

import com.miaoyongzheng.configs.ApplicationDatabase
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

abstract class BaseSchema(private val table: Table) {

    private val database: Database = ApplicationDatabase.database

    init {
        create()
    }

    fun drop() {
        transaction(database) {
            SchemaUtils.drop(table)
        }
    }

    fun create() {
        transaction(database) {
            SchemaUtils.create(table)
        }
    }

    fun deleteAll() = transaction {
        transaction(database) {
            table.deleteAll()
        }
    }

    fun getAll() = transaction {
        table.selectAll()
    }

    suspend fun <T> dbQuery(content: () -> T) = newSuspendedTransaction(Dispatchers.IO) {
        content()
    }
}