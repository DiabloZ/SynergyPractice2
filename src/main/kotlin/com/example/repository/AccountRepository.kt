package com.example.repository

import com.example.db.Tables.Accounts
import com.example.db.Tables.Transactions
import com.example.LogConstants.ACCOUNT_CREATED
import com.example.LogConstants.TRANSFER_INITIATED
import com.example.LogConstants.INVALID_TRANSFER_AMOUNT
import com.example.LogConstants.NULL_ACCOUNTS
import com.example.LogConstants.INSUFFICIENT_FUNDS
import com.example.LogConstants.TRANSFER_RECORDED
import com.example.models.ModelConstants.DEFAULT_CURRENCY
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import org.slf4j.LoggerFactory

data class Account(val id: Int, val userId: Int, val currency: String, val balance: Long)

object AccountRepository {
    private val logger = LoggerFactory.getLogger(AccountRepository::class.java)

    fun create(userId: Int, currency: String = DEFAULT_CURRENCY): Account {
        val id = transaction {
            Accounts.insert {
                it[Accounts.userId] = userId
                it[Accounts.currency] = currency
                it[Accounts.balance] = 0L
            } get Accounts.id
        }
        val acc = Account(id, userId, currency, 0L)
        logger.info(ACCOUNT_CREATED, acc.id, acc.userId, acc.currency)
        return acc
    }

    fun findById(id: Int): Account? = transaction {
        Accounts.select { Accounts.id eq id }
            .map { Account(it[Accounts.id], it[Accounts.userId], it[Accounts.currency], it[Accounts.balance]) }
            .singleOrNull()
    }

    fun findByUser(userId: Int): List<Account> = transaction {
        Accounts.select { Accounts.userId eq userId }
            .map { Account(it[Accounts.id], it[Accounts.userId], it[Accounts.currency], it[Accounts.balance]) }
    }

    fun transfer(fromId: Int?, toId: Int?, amount: Long, description: String?): Boolean {
        logger.info(TRANSFER_INITIATED, fromId, toId, amount)
        return transaction {
            if (amount <= 0) {
                logger.warn(INVALID_TRANSFER_AMOUNT)
                return@transaction false
            }
            val from = fromId?.let { Accounts.select { Accounts.id eq it }.forUpdate().singleOrNull() }
            val to = toId?.let { Accounts.select { Accounts.id eq it }.forUpdate().singleOrNull() }

            if (from == null && to == null) {
                logger.warn(NULL_ACCOUNTS)
                return@transaction false
            }
            if (from != null) {
                val curBal = from[Accounts.balance]
                if (curBal < amount) {
                    logger.warn(INSUFFICIENT_FUNDS, from[Accounts.id], curBal, amount)
                    return@transaction false
                }
                Accounts.update({ Accounts.id eq from[Accounts.id] }) {
                    with(SqlExpressionBuilder) {
                        it.update(Accounts.balance, Accounts.balance - amount)
                    }
                }
            }
            if (to != null) {
                Accounts.update({ Accounts.id eq to[Accounts.id] }) {
                    with(SqlExpressionBuilder) {
                        it.update(Accounts.balance, Accounts.balance + amount)
                    }
                }
            }
            Transactions.insert {
                it[Transactions.fromAccount] = fromId
                it[Transactions.toAccount] = toId
                it[Transactions.amount] = amount
                it[Transactions.description] = description
                it[Transactions.createdAt] = Instant.now().toEpochMilli()
            }
            logger.info(TRANSFER_RECORDED, fromId, toId, amount)
            true
        }
    }
}
