package com.example.cryptotrade.domain.repository

import com.dynamic.sdk.android.Models.BaseWallet
import com.example.cryptotrade.domain.model.TransactionRequest

interface WalletRepository {

    suspend fun getWallet(): BaseWallet

    suspend fun getNetwork(): String?

    suspend fun sendTransaction(request: TransactionRequest): String

    suspend fun getBalance(): String

    suspend fun switchTestnet()
}