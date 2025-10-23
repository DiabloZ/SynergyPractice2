package com.example.models

import com.example.models.ModelConstants.DEFAULT_CURRENCY
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(val id: Int, val username: String, val role: String)

@Serializable
data class RegisterRequest(val username: String, val password: String)

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class TokenResponse(val token: String)

@Serializable
data class AccountDTO(val id: Int, val userId: Int, val currency: String, val balanceCents: Long)

@Serializable
data class CreateAccountRequest(val currency: String = DEFAULT_CURRENCY)

@Serializable
data class PaymentRequest(
	val fromAccountId: Int? = null,
	val toAccountId: Int?,
	val amountCents: Long,
	val description: String? = null
)
