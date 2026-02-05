package com.example.cryptotrade.presentation.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.cryptotrade.domain.repository.AuthRepository
import com.example.cryptotrade.presentation.base.BaseViewModel
import com.example.cryptotrade.presentation.login.state.LoginEvent
import com.example.cryptotrade.presentation.login.state.LoginSideEffect
import com.example.cryptotrade.presentation.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginState, LoginSideEffect>(LoginState()) {

    companion object {

        private const val OTP_LENGTH = 6
    }

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.SendEmail -> sendEmail(event.email)
            is LoginEvent.OnChangeEmail -> onChangeEmail(event.text)
            is LoginEvent.OnChangeOtp -> onChangeOtp(event.text)
            is LoginEvent.SendOtp -> sendOtp(event.otp)
            LoginEvent.DismissBottomSheet -> closeBottomSheet()
            LoginEvent.DismissError -> closeErrorDialog()
        }
    }

    private fun sendEmail(email: String) {
        if (currentState.otpSent) {
            updateState { copy(isShowOtpBottomSheet = true) }
        } else {
            if (!(currentState.email.isValidEmail())) {
                updateState { copy(isEmailError = true) }
                return
            }

            viewModelScope.launch {
                runCatching {
                    updateState { copy(isLoading = true, isEmailError = false) }
                    authRepository.sendOtp(email)
                }.onSuccess {
                    updateState {
                        copy(
                            otpSent = true,
                            isShowOtpBottomSheet = true,
                            isLoading = false
                        )
                    }
                }.onFailure { throwable ->
                    updateState { copy(error = throwable, isLoading = false) }
                    Log.d("LOG_ERROR", throwable.message.toString())
                }
            }
        }
    }

    private fun sendOtp(otp: String) {
        if (currentState.otpCode.length != OTP_LENGTH) return

        viewModelScope.launch {
            runCatching {
                authRepository.verifyOtp(otp)
            }.onSuccess {
                sendEffect(LoginSideEffect.NavigateToWallet)
            }.onFailure { throwable ->
                updateState { copy(error = throwable, isLoading = false) }
                Log.d("LOG_ERROR", throwable.message.toString())
            }
        }
    }

    private fun onChangeEmail(text: String) {
        updateState { copy(email = text) }
    }

    private fun onChangeOtp(text: String) {
        if (currentState.otpCode.length == OTP_LENGTH) return

        updateState { copy(otpCode = text) }
    }

    private fun closeBottomSheet() {
        updateState { copy(isShowOtpBottomSheet = false) }
    }

    private fun closeErrorDialog() {
        updateState { copy(error = null) }
    }

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun String.isValidEmail(): Boolean =
        isNotBlank() && EMAIL_REGEX.matches(this)
}