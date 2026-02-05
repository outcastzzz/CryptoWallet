package com.example.cryptotrade.presentation.wallet.state

import com.dynamic.sdk.android.Models.BaseWallet

data class WalletState(
    val wallet: BaseWallet? = null,
    val balance: String = "",
    val network: String = "",
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = true,
    val error: Throwable? = null
)

sealed interface WalletSideEffect {
    data object NavigateToSend : WalletSideEffect
    data object NavigateToLogin : WalletSideEffect
    data object ShowSnackBar : WalletSideEffect
}

sealed interface WalletEvent {
    data object SendClick : WalletEvent
    data object Refresh : WalletEvent
    data object Logout : WalletEvent
    data class CopyAddress(val walletAddress: String) : WalletEvent
}