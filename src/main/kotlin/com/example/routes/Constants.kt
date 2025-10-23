package com.example.routes

object RoutesConstants {
    const val API_AUTH_PATH = "/api/auth"
    const val REGISTER_PATH = "/register"
    const val LOGIN_PATH = "/login"

    const val INVALID_CREDENTIALS_MESSAGE = "Invalid credentials"

    const val JWT_AUTH = "auth-jwt"
    const val API_ACCOUNTS_PATH = "/api/accounts"
    const val USER_ID_CLAIM = "userId"

    const val API_PAYMENTS_PATH = "/api/payments"

    const val STATUS_KEY = "status"
    const val INVALID_STATUS = "invalid"
    const val OK_STATUS = "ok"
    const val FAILED_STATUS = "failed"
    const val FORBIDDEN_TRANSFER_MESSAGE = "cannot transfer from other\'s account"
    const val FORBIDDEN_DEPOSIT_MESSAGE = "cannot deposit to other\'s account"
}
