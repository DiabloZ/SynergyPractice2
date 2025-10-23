package com.example.routes

import com.example.models.*
import com.example.services.AuthService
import com.example.plugins.JwtConfig
import com.example.routes.RoutesConstants.API_AUTH_PATH
import com.example.routes.RoutesConstants.INVALID_CREDENTIALS_MESSAGE
import com.example.routes.RoutesConstants.LOGIN_PATH
import com.example.routes.RoutesConstants.REGISTER_PATH
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.authRoutes() {
    route(API_AUTH_PATH) {
        post(REGISTER_PATH) {
            val req = call.receive<RegisterRequest>()
            val user = AuthService.register(req.username, req.password)
            call.respond(HttpStatusCode.Created, UserDTO(user.id, user.username, user.role))
        }
        post(LOGIN_PATH) {
            val req = call.receive<LoginRequest>()
            val u = AuthService.authenticate(req.username, req.password)
            if (u == null) {
                call.respond(HttpStatusCode.Unauthorized, INVALID_CREDENTIALS_MESSAGE)
            } else {
                val token = JwtConfig.makeToken(u.username, u.id)
                call.respond(TokenResponse(token))
            }
        }
    }
}
