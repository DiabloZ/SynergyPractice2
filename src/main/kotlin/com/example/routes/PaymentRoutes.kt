package com.example.routes

import com.example.LogConstants.FORBIDDEN_DEPOSIT_ATTEMPT
import com.example.LogConstants.FORBIDDEN_TRANSFER_ATTEMPT
import com.example.models.PaymentRequest
import com.example.repository.AccountRepository
import com.example.routes.RoutesConstants.API_PAYMENTS_PATH
import com.example.routes.RoutesConstants.FAILED_STATUS
import com.example.routes.RoutesConstants.FORBIDDEN_DEPOSIT_MESSAGE
import com.example.routes.RoutesConstants.FORBIDDEN_TRANSFER_MESSAGE
import com.example.routes.RoutesConstants.JWT_AUTH
import com.example.routes.RoutesConstants.OK_STATUS
import com.example.routes.RoutesConstants.STATUS_KEY
import com.example.routes.RoutesConstants.USER_ID_CLAIM
import com.example.services.PaymentService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Routing.paymentRoutes() {
	val logger = LoggerFactory.getLogger("PaymentRoutes")

	authenticate(JWT_AUTH) {
		post(API_PAYMENTS_PATH) {
			val principal = call.principal<JWTPrincipal>() ?: return@post
			val userId = principal.payload.getClaim(USER_ID_CLAIM).asInt()
			val req = call.receive<PaymentRequest>()

			if (req.fromAccountId != null) {
				val fromAccount = AccountRepository.findById(req.fromAccountId)
				if (fromAccount?.userId != userId) {
					logger.warn(FORBIDDEN_TRANSFER_ATTEMPT, userId, req.fromAccountId)
					call.respond(HttpStatusCode.Forbidden, mapOf(STATUS_KEY to FORBIDDEN_TRANSFER_MESSAGE))
					return@post
				}
			} else {
				val toAccount = req.toAccountId?.let { AccountRepository.findById(id = it) }
				if (toAccount?.userId != userId) {
					logger.warn(FORBIDDEN_DEPOSIT_ATTEMPT, userId, req.toAccountId)
					call.respond(HttpStatusCode.Forbidden, mapOf(STATUS_KEY to FORBIDDEN_DEPOSIT_MESSAGE))
					return@post
				}
			}


			val ok = PaymentService.processPayment(
				fromAccountId = req.fromAccountId,
				toAccountId = req.toAccountId,
				amountCents = req.amountCents,
				description = req.description
			)
			if (ok) {
				call.respond(HttpStatusCode.OK, mapOf(STATUS_KEY to OK_STATUS))
			} else {
				call.respond(HttpStatusCode.BadRequest, mapOf(STATUS_KEY to FAILED_STATUS))
			}
		}
	}
}
