package com.example.cryptotrade.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotrade.presentation.base.components.ErrorDialog
import com.example.cryptotrade.presentation.base.components.OtpBottomSheet
import com.example.cryptotrade.presentation.base.components.PrimaryButton
import com.example.cryptotrade.presentation.login.state.LoginEvent
import com.example.cryptotrade.presentation.login.state.LoginState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
) {
    when {
        state.error != null -> {
            ErrorDialog(
                onDismiss = { onEvent(LoginEvent.DismissError) },
                message = "Someting gone wrong",
                title = "Please try later",
                buttonText = "Ok",
            )
        }

        else -> {
            if (state.isShowOtpBottomSheet) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                OtpBottomSheet(
                    code = state.otpCode,
                    sheetState = sheetState,
                    onDismiss = { onEvent(LoginEvent.DismissBottomSheet) },
                    onCodeChange = { code -> onEvent(LoginEvent.OnChangeOtp(code)) },
                    onConfirmClick = { onEvent(LoginEvent.SendOtp(state.otpCode)) },
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Crypto Wallet",
                    modifier = Modifier.padding(top = 60.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )

                Text(
                    text = "Please sign in to continue",
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { text -> onEvent(LoginEvent.OnChangeEmail(text)) },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    isError = state.isEmailError,
                )

                PrimaryButton(
                    title = "Send Email OTP",
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { onEvent(LoginEvent.SendEmail(state.email)) },
                    isLoading = state.isLoading,
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginState(
                email = "user@example.com",
                otpCode = "",
                isLoading = false,
                isShowOtpBottomSheet = false
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
fun LoginScreenLoadingPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginState(
                email = "user@example.com",
                otpCode = "",
                isLoading = true,
                isShowOtpBottomSheet = false
            ),
            onEvent = {},
        )
    }
}

@Preview
@Composable
fun LoginScreenWithBottomSheetPreview() {
    MaterialTheme {
        LoginScreen(
            state = LoginState(
                email = "user@example.com",
                otpCode = "123456",
                isLoading = false,
                isShowOtpBottomSheet = true
            ),
            onEvent = {},
        )
    }
}
