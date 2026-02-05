package com.example.cryptotrade.core.common

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

object EthUtils {
    private val WEI_IN_ETH = BigDecimal("1000000000000000000")

    /**
     * Валидация Ethereum адреса
     */
    fun isValidAddress(address: String): Boolean {
        return address.matches(Regex("^0x[a-fA-F0-9]{40}$"))
    }

    /**
     * Валидация суммы
     */
    fun isValidAmount(amount: BigInteger): Boolean {
        return try {
            val value = BigDecimal(amount)
            value > BigDecimal.ZERO
        } catch (e: Exception) {
            false
        }
    }

    fun ethToWei(ethAmount: BigInteger): BigInteger = ethAmount.multiply(WEI_IN_ETH.toBigInteger())
}

/** Mask wallet address for given length */
fun String.maskWalletAddress(
    startChars: Int = 6,
    endChars: Int = 4,
    mask: String = "...",
): String {
    if (length <= startChars + endChars) return this

    val start = take(startChars)
    val end = takeLast(endChars)

    return "$start$mask$end"
}