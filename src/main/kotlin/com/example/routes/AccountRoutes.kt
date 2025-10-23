package com.example.routes

import com.example.models.*
import com.example.repository.AccountRepository
import com.example.routes.RoutesConstants.API_ACCOUNTS_PATH
import com.example.routes.RoutesConstants.JWT_AUTH
import com.example.routes.RoutesConstants.USER_ID_CLAIM
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.accountRoutes() {
    authenticate(JWT_AUTH) {
        route(API_ACCOUNTS_PATH) {
            post {
                val principal = call.principal<io.ktor.server.auth.jwt.JWTPrincipal>()!!
                val userId = principal.payload.getClaim(USER_ID_CLAIM).asInt()
                val req = call.receive<CreateAccountRequest>()
                val acc = AccountRepository.create(userId, req.currency)
                call.respond(HttpStatusCode.Created, AccountDTO(acc.id, acc.userId, acc.currency, acc.balance))
            }
            get {
                val principal = call.principal<io.ktor.server.auth.jwt.JWTPrincipal>()!!
                val userId = principal.payload.getClaim(USER_ID_CLAIM).asInt()
                val list = AccountRepository.findByUser(userId).map { AccountDTO(it.id, it.userId, it.currency, it.balance) }
                call.respond(list)
            }
        }
    }
}
