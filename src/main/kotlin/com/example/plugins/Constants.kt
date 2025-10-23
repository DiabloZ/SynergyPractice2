package com.example.plugins

object PluginsConstants {
    const val ROUTING_LOGGER = "Routing"

    const val STATIC_ROUTE = "/"
    const val STATIC_RESOURCES = "static"
    const val STATIC_DEFAULT_RESOURCE = "static/index.html"

    const val JWT_SECRET = "very_secret_change_in_prod"
    const val JWT_ISSUER = "fintech.example"
    const val JWT_SUBJECT = "Authentication"
    const val JWT_USERNAME_CLAIM = "username"
    const val JWT_USER_ID_CLAIM = "userId"
    const val JWT_AUTH_METHOD = "auth-jwt"
}
