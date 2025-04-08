package com.miaoyongzheng.configs

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

object ApplicationDatabase {
    lateinit var database: Database

    fun init(environment: ApplicationEnvironment) {
        val config = environment.config.config("mysql")
        val url = config.property("url").getString()
        val driver = config.property("driver").getString()
        val username = config.property("username").getString()
        val password = config.property("password").getString()

        database = Database.connect(
            url = url,
            driver = driver,
            user = username,
            password = password
        )
    }
}


fun Application.configureDatabases() {
    ApplicationDatabase.init(environment)
}

