package com.example.cryptotrade.presentation.send

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.domain.model.TransactionRequest
import com.example.cryptotrade.domain.repository.WalletRepository
import com.example.cryptotrade.presentation.base.BaseViewModel
import com.example.cryptotrade.presentation.send.state.SendEvent
import com.example.cryptotrade.presentation.send.state.SendSideEffect
import com.example.cryptotrade.presentation.send.state.SendState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : BaseViewModel<SendState, SendSideEffect>(SendState()) {

    fun handleEvent(event: SendEvent) {
        when (event) {
            is SendEvent.AddressChange -> onAddressChange(event.address)
            is SendEvent.AmountChange -> onAmountChange(event.amount)
            SendEvent.BackClick -> sendEffect(SendSideEffect.NavigateBack)
            is SendEvent.SendClick -> send()
        }
    }

    private fun onAddressChange(address: String) {
        updateState { copy(toAddress = address, error = null) }
    }

    private fun onAmountChange(amount: String) {
        val filtered = amount.filter { it.isDigit() || it == '.' }
        updateState { copy(amount = filtered, error = null) }
    }

    private fun send() {
        if (!currentState.canSend) return

        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            runCatching {
                val hash = walletRepository.sendTransaction(
                    request = TransactionRequest(
                        toAddress = currentState.toAddress,
                        amount = currentState.amount.toBigInteger(),
                    )
                )

                updateState { copy(txHash = hash, isLoading = false) }
            }.onFailure { throwable ->
                Log.d("LOG_ERROR", throwable.message.toString())
                updateState { copy(error = throwable.localizedMessage, isLoading = false) }
            }
        }
    }
}
