package com.example.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.db.Tables.Users
import com.example.db.Tables.Accounts
import com.example.db.Tables.Transactions
import com.example.db.DbConstants.JDBC_URL
import com.example.db.DbConstants.JDBC_DRIVER
import com.example.db.DbConstants.TRANSACTION_ISOLATION

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = JDBC_URL
            driverClassName = JDBC_DRIVER
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = TRANSACTION_ISOLATION
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Users, Accounts, Transactions)
        }
    }
}
