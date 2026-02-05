package com.example.cryptotrade.presentation.send.state

data class SendState(
    val toAddress: String = "",
    val amount: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val txHash: String? = null,
) {
    val isValidAddress: Boolean
        get() = toAddress.isEmpty() || toAddress.matches(Regex("^0x[a-fA-F0-9]{40}$"))

    val isValidAmount: Boolean
        get() = amount.isEmpty() || amount.toDoubleOrNull()?.let { it > 0 } == true

    val canSend: Boolean
        get() = toAddress.matches(Regex("^0x[a-fA-F0-9]{40}$")) &&
                amount.toDoubleOrNull()?.let { it > 0 } == true &&
                !isLoading
}

sealed interface SendSideEffect {
    data object NavigateBack : SendSideEffect
}

sealed interface SendEvent {
    data class AddressChange(val address: String) : SendEvent
    data class AmountChange(val amount: String) : SendEvent
    data object SendClick : SendEvent
    data object BackClick : SendEvent
}