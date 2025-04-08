package com.miaoyongzheng.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.miaoyongzheng.utils.Const.Companion.CLAIM_USER_ID
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import java.util.*


class AuthUtil {
    companion object {
        // Generate JWT Token
        fun generateJWTToken(environment: ApplicationEnvironment, userId: Int): String =
            environment.config.config("jwt").run {
                val secret = property("secret").getString()
                val issuer = property("issuer").getString()
                val audience = property("audience").getString()
                val realm = property("realm").getString()
                val token = JWT.create()
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .withClaim(CLAIM_USER_ID, userId)
                    .withExpiresAt(Date(System.currentTimeMillis() + 6000000))
                    .sign(Algorithm.HMAC256(secret))
                return token
            }

        fun getJWTUserId(call: RoutingCall): Int? {
            return call.principal<JWTPrincipal>()?.payload?.getClaim(CLAIM_USER_ID)?.asInt()
        }
    }


}