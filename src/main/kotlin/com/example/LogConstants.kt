package com.example

object LogConstants {
    const val APP_START = "🚀🚀 Starting FintechApp (Ktor) 🚀🚀"
    const val DB_INIT = "📦📦 Initializing database... 📦📦"
    const val DB_INIT_DONE = "✅✅ Database initialization complete ✅✅"
    const val ROUTING_CONFIGURED = "🧭🧭 Routing configured, server ready to accept requests 🧭🧭"

    const val REGISTER_ATTEMPT = "👤👤 Attempting to register user '{}' 👤👤"
    const val USER_REGISTERED = "✅✅ User registered: id={} username={} ✅✅"
    const val AUTH_ATTEMPT = "🔐🔐 Authenticating user '{}' 🔐🔐"
    const val AUTH_SUCCESS = "✅✅ Authentication success for user id={} ✅✅"
    const val AUTH_FAILED = "❌❌ Authentication failed for username={} ❌❌"

    const val USER_CREATED = "✅✅ Created user: id={} username={} ✅✅"
    const val PASSWORD_VERIFIED = "🔑🔑 Password verified for user id={} 🔑🔑"
    const val USER_NOT_FOUND = "🤷🤷 User not found: {} 🤷🤷"
    const val INVALID_PASSWORD = "❌❌ Invalid password for user {} ❌❌"

    const val ACCOUNT_CREATED = "✅✅ Account created: id={} userId={} currency={} ✅✅"
    const val TRANSFER_INITIATED = "💸💸 Initiating transfer from={} to={} amount={} 💸💸"
    const val TRANSFER_RECORDED = "✅✅ Transfer recorded: from={} to={} amount={} ✅✅"
    const val INSUFFICIENT_FUNDS = "💰💰 Insufficient funds: accountId={} balance={} required={} 💰💰"
    const val INVALID_TRANSFER_AMOUNT = "❌❌ Transfer amount <= 0 ❌❌"
    const val NULL_ACCOUNTS = "🤷🤷 Both from and to accounts are null 🤷🤷"
    const val FORBIDDEN_TRANSFER_ATTEMPT = "🚫🚫 User {} attempted to transfer from account {} which does not belong to them 🚫🚫"
    const val FORBIDDEN_DEPOSIT_ATTEMPT = "🚫🚫 User {} attempted to deposit to account {} which does not belong to them 🚫🚫"

    const val PROCESSING_PAYMENT = "💳💳 Processing payment from={} to={} amountCents={} desc={} 💳💳"
    const val PAYMENT_SUCCESS = "✅✅ Payment processed successfully ✅✅"
    const val PAYMENT_FAILED = "❌❌ Payment failed or rolled back ❌❌"

    const val CONFIGURING_ROUTES = "🧭🧭 Configuring routes... 🧭🧭"
    const val ROUTES_CONFIGURED = "✅✅ Routes configured ✅✅"
}
