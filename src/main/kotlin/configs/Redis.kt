package com.miaoyongzheng.configs

import io.ktor.server.application.*
import redis.clients.jedis.HostAndPort
import redis.clients.jedis.UnifiedJedis

object Redis {
    lateinit var jedis: UnifiedJedis

    fun init(environment: ApplicationEnvironment) {
        val config = environment.config.config("redis")
        val host = config.property("host").getString()
        val port = config.property("port").getString()

        this.jedis = UnifiedJedis(HostAndPort(host, port.toInt()))
    }
}

fun Application.configureRedis() {
    Redis.init(environment)
}

