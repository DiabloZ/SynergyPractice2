package com.example.services

import com.example.LogConstants.AUTH_ATTEMPT
import com.example.LogConstants.AUTH_FAILED
import com.example.LogConstants.AUTH_SUCCESS
import com.example.LogConstants.REGISTER_ATTEMPT
import com.example.LogConstants.USER_REGISTERED
import com.example.repository.User
import com.example.repository.UserRepository
import org.slf4j.LoggerFactory

object AuthService {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun register(username: String, password: String): User {
        logger.info(REGISTER_ATTEMPT, username)
        val user = UserRepository.create(username, password)
        logger.info(USER_REGISTERED, user.id, user.username)
        return user
    }

    fun authenticate(username: String, password: String): User? {
        logger.info(AUTH_ATTEMPT, username)
        val user = UserRepository.verifyCredentials(username, password)
        if (user != null) logger.info(AUTH_SUCCESS, user.id) else logger.warn(AUTH_FAILED, username)
        return user
    }
}
