package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.PluginsConstants.JWT_AUTH_METHOD
import com.example.plugins.PluginsConstants.JWT_ISSUER
import com.example.plugins.PluginsConstants.JWT_SECRET
import com.example.plugins.PluginsConstants.JWT_SUBJECT
import com.example.plugins.PluginsConstants.JWT_USERNAME_CLAIM
import com.example.plugins.PluginsConstants.JWT_USER_ID_CLAIM
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object JwtConfig {
    private const val secret = JWT_SECRET
    private const val issuer = JWT_ISSUER
    private const val validityInMs = 36_000_00 * 24 // 24 hours
    private val algorithm = Algorithm.HMAC256(secret)

    fun makeToken(username: String, userId: Int): String = JWT.create()
        .withSubject(JWT_SUBJECT)
        .withIssuer(issuer)
        .withClaim(JWT_USERNAME_CLAIM, username)
        .withClaim(JWT_USER_ID_CLAIM, userId)
        .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
        .sign(algorithm)

    fun configureKtorAuth(app: Application) {
        app.install(Authentication) {
            jwt(JWT_AUTH_METHOD) {
                verifier(JWT.require(algorithm).withIssuer(issuer).build())
                validate { credential ->
                    if (credential.payload.getClaim(JWT_USERNAME_CLAIM).asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else null
                }
            }
        }
    }
}

fun Application.configureSecurity() {
    JwtConfig.configureKtorAuth(this)
}
