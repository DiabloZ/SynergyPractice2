package com.example.db

import com.example.models.ModelConstants.DEFAULT_CURRENCY
import org.jetbrains.exposed.sql.Table

object Tables {
    object Users : Table("users") {
        val id = integer("id").autoIncrement()
        val username = varchar("username", 50).uniqueIndex()
        val passwordHash = varchar("password_hash", 60)
        val role = varchar("role", 20).default("user")
        override val primaryKey = PrimaryKey(id)
    }

    object Accounts : Table("accounts") {
        val id = integer("id").autoIncrement()
        val userId = integer("user_id").references(Users.id)
        val currency = varchar("currency", 10).default(DEFAULT_CURRENCY)
        val balance = long("balance").default(0L) // cents
        override val primaryKey = PrimaryKey(id)
    }

    object Transactions : Table("transactions") {
        val id = integer("id").autoIncrement()
        val fromAccount = integer("from_account").references(Accounts.id).nullable()
        val toAccount = integer("to_account").references(Accounts.id).nullable()
        val amount = long("amount") // cents
        val description = varchar("description", 255).nullable()
        val createdAt = long("created_at")
        override val primaryKey = PrimaryKey(id)
    }
}
