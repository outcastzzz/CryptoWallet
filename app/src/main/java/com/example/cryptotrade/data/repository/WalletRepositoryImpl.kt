package com.example.cryptotrade.data.repository

import com.dynamic.sdk.android.Models.BaseWallet
import com.example.cryptotrade.data.web3.DynamicSdkWrapper
import com.example.cryptotrade.domain.model.TransactionRequest
import com.example.cryptotrade.domain.repository.WalletRepository
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val sdkWrapper: DynamicSdkWrapper,
) : WalletRepository {

    override suspend fun getWallet(): BaseWallet = sdkWrapper.getEvmWallet()
        ?: throw WalletNotFoundException()

    override suspend fun getNetwork(): String? = sdkWrapper.getNetwork()

    override suspend fun sendTransaction(request: TransactionRequest): String =
        sdkWrapper.sendTransaction(
            to = request.toAddress,
            amountEth = request.amount,
        )

    override suspend fun getBalance(): String = sdkWrapper.getBalance()

    override suspend fun switchTestnet() = sdkWrapper.switchTestnet()
}

class WalletNotFoundException : Exception("No EVM wallet found")
