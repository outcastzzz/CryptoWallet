package com.example.cryptotrade.presentation.login.state

data class LoginState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val otpCode: String = "",
    val otpSent: Boolean = false,
    val isShowOtpBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)

sealed interface LoginEvent {
    data class OnChangeEmail(val text: String) : LoginEvent
    data class OnChangeOtp(val text: String) : LoginEvent
    data class SendEmail(val email: String) : LoginEvent
    data class SendOtp(val otp: String) : LoginEvent
    data object DismissBottomSheet : LoginEvent
    data object DismissError : LoginEvent
}

sealed interface LoginSideEffect {
    data object NavigateToWallet : LoginSideEffect
}