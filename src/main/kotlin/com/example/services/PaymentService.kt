package com.example.services

import com.example.LogConstants.PAYMENT_FAILED
import com.example.LogConstants.PAYMENT_SUCCESS
import com.example.LogConstants.PROCESSING_PAYMENT
import com.example.repository.AccountRepository
import org.slf4j.LoggerFactory

object PaymentService {
    private val logger = LoggerFactory.getLogger(PaymentService::class.java)

    fun processPayment(fromAccountId: Int?, toAccountId: Int?, amountCents: Long, description: String?): Boolean {
        logger.info(PROCESSING_PAYMENT, fromAccountId, toAccountId, amountCents, description)
        val result = AccountRepository.transfer(fromAccountId, toAccountId, amountCents, description)
        if (result) logger.info(PAYMENT_SUCCESS) else logger.warn(PAYMENT_FAILED)
        return result
    }
}
