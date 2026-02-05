package com.example.cryptotrade.domain.model

import java.math.BigInteger

data class TransactionRequest(
    val toAddress: String,
    val amount: BigInteger,
)