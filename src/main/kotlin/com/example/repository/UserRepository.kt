package com.example.repository

import com.example.db.Tables.Users
import com.example.LogConstants.USER_CREATED
import com.example.LogConstants.PASSWORD_VERIFIED
import com.example.LogConstants.USER_NOT_FOUND
import com.example.LogConstants.INVALID_PASSWORD
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

data class User(val id: Int, val username: String, val passwordHash: String, val role: String)

object UserRepository {
    private val logger = LoggerFactory.getLogger(UserRepository::class.java)

    fun create(username: String, password: String, role: String = "user"): User {
        val hash = BCrypt.hashpw(password, BCrypt.gensalt())
        val id = transaction {
            Users.insert {
                it[Users.username] = username
                it[Users.passwordHash] = hash
                it[Users.role] = role
            } get Users.id
        }
        val user = User(id, username, hash, role)
        logger.info(USER_CREATED, user.id, user.username)
        return user
    }

    fun findByUsername(username: String): User? = transaction {
        Users.select { Users.username eq username }
            .map { User(it[Users.id], it[Users.username], it[Users.passwordHash], it[Users.role]) }
            .singleOrNull()
    }

    fun verifyCredentials(username: String, password: String): User? {
        val u = findByUsername(username) ?: run {
            logger.warn(USER_NOT_FOUND, username)
            return null
        }
        return if (BCrypt.checkpw(password, u.passwordHash)) {
            logger.info(PASSWORD_VERIFIED, u.id)
            u
        } else {
            logger.warn(INVALID_PASSWORD, username)
            null
        }
    }
}
