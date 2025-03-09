package com.miaoyongzheng

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.ucasoft.ktor.simpleCache.SimpleCache
import com.ucasoft.ktor.simpleCache.cacheOutput
import com.ucasoft.ktor.simpleMemoryCache.*
import com.ucasoft.ktor.simpleRedisCache.*
import dev.inmo.krontab.builder.*
import io.github.flaxoos.ktor.server.plugins.taskscheduling.*
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.*
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.redis.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.server.websocket.*
import io.ktor.sse.*
import io.ktor.websocket.*
import java.time.Duration
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.*

fun Application.configureAdministration() {
    install(TaskScheduling) {
        // Choose task manager config based on your chosen task manager dependencies
//        redis { // <-- given no name, this will be the default manager
//            connectionPoolInitialSize = 1
//            host = "host"
//            port = 6379
//            username = "my_username"
//            password = "my_password"
//            connectionAcquisitionTimeoutMs = 1_000
//            lockExpirationMs = 60_000
//        }
//        jdbc("my jdbc manager") { // <-- given a name, a manager can be explicitly selected for a task
//            database = org.jetbrains.exposed.sql.Database.connect(
//                url = "jdbc:postgresql://host:port",
//                driver = "org.postgresql.Driver",
//                user = "my_username",
//                password = "my_password"
//            ).also {
//                transaction { SchemaUtils.create(DefaultTaskLockTable) }
//            }
//        }
//        mongoDb("my mongodb manager") {
//            databaseName = "test"
//            client = MongoClient.create("mongodb://host:port")
//        }

        task { // if no taskManagerName is provided, the task would be assigned to the default manager
            name = "My task"
            task = { taskExecutionTime ->
                log.info("My task is running: $taskExecutionTime")
            }
            kronSchedule = {
                hours {
                    from(0).every(12)
                }
                minutes {
                    from(10).every(30)
                }
            }
            concurrency = 2
        }

//        task(taskManagerName = "my jdbc manager") {
//            name = "My Jdbc task"
//            // rest of task config
//        }
    }
}
