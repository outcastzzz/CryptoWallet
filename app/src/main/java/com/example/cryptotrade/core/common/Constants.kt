package com.example.cryptotrade.core.common

object Constants {
    const val SEPOLIA_CHAIN_ID = 11155111L
    const val SEPOLIA_CHAIN_NAME = "Sepolia"
    const val SEPOLIA_EXPLORER_URL = "https://sepolia.etherscan.io"
    const val EVM_CHAIN_UPPERCASE = "EVM"

    fun getTransactionUrl(txHash: String): String {
        return "$SEPOLIA_EXPLORER_URL/tx/$txHash"
    }

    fun getAddressUrl(address: String): String {
        return "$SEPOLIA_EXPLORER_URL/address/$address"
    }
}