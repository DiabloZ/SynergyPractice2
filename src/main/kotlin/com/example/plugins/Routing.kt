package com.example.plugins

import com.example.LogConstants.CONFIGURING_ROUTES
import com.example.LogConstants.ROUTES_CONFIGURED
import com.example.plugins.PluginsConstants.ROUTING_LOGGER
import com.example.plugins.PluginsConstants.STATIC_DEFAULT_RESOURCE
import com.example.plugins.PluginsConstants.STATIC_RESOURCES
import com.example.plugins.PluginsConstants.STATIC_ROUTE
import com.example.routes.accountRoutes
import com.example.routes.authRoutes
import com.example.routes.paymentRoutes
import io.ktor.server.application.*
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Application.configureRouting() {
    val logger = LoggerFactory.getLogger(ROUTING_LOGGER)
    logger.info(CONFIGURING_ROUTES)
    routing {
        authRoutes()
        accountRoutes()
        paymentRoutes()
        staticResources()
    }
    logger.info(ROUTES_CONFIGURED)
}

fun Routing.staticResources() {
    static(STATIC_ROUTE) {
        resources(STATIC_RESOURCES)
        defaultResource(STATIC_DEFAULT_RESOURCE)
    }
}
