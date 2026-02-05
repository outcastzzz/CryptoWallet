package com.example.cryptotrade.presentation.wallet

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.domain.clipboard.ClipboardManager
import com.example.cryptotrade.domain.repository.AuthRepository
import com.example.cryptotrade.domain.repository.WalletRepository
import com.example.cryptotrade.presentation.base.BaseViewModel
import com.example.cryptotrade.presentation.wallet.state.WalletEvent
import com.example.cryptotrade.presentation.wallet.state.WalletSideEffect
import com.example.cryptotrade.presentation.wallet.state.WalletState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val authRepository: AuthRepository,
    private val clipboardManager: ClipboardManager,
) : BaseViewModel<WalletState, WalletSideEffect>(WalletState()) {

    companion object {

        private const val BALANCE_TAG = "BALANCE"
        private const val NETWORK_TAG = "NETWORK"
        private const val TESTNET_TAG = "TESTNET"
    }

    init {
        loadWallet(isRefreshing = false)
    }

    fun handleEvent(event: WalletEvent) {
        when (event) {
            WalletEvent.Logout -> logout()
            WalletEvent.Refresh -> loadWallet(isRefreshing = true)
            WalletEvent.SendClick -> onSendClick()
            is WalletEvent.CopyAddress -> copyAddress(event.walletAddress)
        }
    }

    private fun loadWallet(isRefreshing: Boolean) {
        updateState { copy(isLoading = !isRefreshing, isRefreshing = isRefreshing) }
        viewModelScope.launch {
            runCatching {
                walletRepository.getWallet()
            }.onSuccess { wallet ->
                safeCall(tag = TESTNET_TAG) {
                    walletRepository.switchTestnet()
                }
                val balance = safeCall(tag = BALANCE_TAG) {
                    walletRepository.getBalance()
                }.orEmpty()
                val network = safeCall(tag = NETWORK_TAG) {
                    walletRepository.getNetwork()
                }.orEmpty()

                updateState {
                    copy(
                        wallet = wallet,
                        balance = balance.ifEmpty { "Error receive balance. Try again Later" },
                        network = network,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }.onFailure { throwable ->
                Log.d("LOG_ERROR", "Wallet error: ${throwable.message}")
                updateState {
                    copy(
                        error = throwable,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            }
        }
    }

    private fun onSendClick() {
        sendEffect(WalletSideEffect.NavigateToSend)
    }

    private fun logout() {
        viewModelScope.launch {
            runCatching {
                authRepository.logout()
            }.onFailure { throwable ->
                Log.d("LOG_ERROR", throwable.message.orEmpty())
            }.also {
                sendEffect(WalletSideEffect.NavigateToLogin)
            }
        }
    }

    private fun copyAddress(address: String) {
        clipboardManager
            .copy(address)
            .also {
                sendEffect(WalletSideEffect.ShowSnackBar)
            }
    }
}
