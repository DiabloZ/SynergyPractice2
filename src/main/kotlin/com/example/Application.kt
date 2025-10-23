package com.example

import com.example.db.DatabaseFactory
import com.example.plugins.configureRouting
import com.example.plugins.configureSecurity
import com.example.plugins.configureSerialization
import com.example.LogConstants.APP_START
import com.example.LogConstants.DB_INIT
import com.example.LogConstants.DB_INIT_DONE
import com.example.LogConstants.ROUTING_CONFIGURED
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

fun main() {
    Bootstrap
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(CallLogging) {
        level = Level.INFO
        format { call ->
            val status = call.response.status() ?: "?"
            val method = call.request.httpMethod.value
            val uri = call.request.uri
            "HTTP $method - $uri - Status: $status"
        }
    }

    val logger = LoggerFactory.getLogger("FintechApp")
    logger.info(APP_START)
    logger.info(DB_INIT)
    logger.info(DB_INIT_DONE)

    configureSerialization()
    configureSecurity()
    configureRouting()

    logger.info(ROUTING_CONFIGURED)
}
