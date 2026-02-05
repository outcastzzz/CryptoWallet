package com.example.cryptotrade.presentation.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpBottomSheet(
    code: String,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Enter OTP code",
    subtitle: String? = null,
    buttonText: String = "Verify",
    codeLength: Int = 6,
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null,
        modifier = modifier,
    ) {
        OtpBottomSheetContent(
            code = code,
            onCodeChange = onCodeChange,
            onConfirmClick = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    onConfirmClick()
                }
            },
            modifier = modifier,
            title = title,
            subtitle = subtitle,
            buttonText = buttonText,
            codeLength = codeLength,
        )
    }
}

@Composable
fun OtpBottomSheetContent(
    code: String,
    onCodeChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Enter OTP code",
    subtitle: String? = null,
    buttonText: String = "Verify",
    codeLength: Int = 6,
    isLoading: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    onResendClick: (() -> Unit)? = null,
    resendText: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp, top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(2.dp),
                ),
        )

        Text(
            text = title,
            modifier = Modifier.padding(top = 24.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        if (subtitle != null) {
            Text(
                text = subtitle,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        OtpInputField(
            code = code,
            onCodeChange = onCodeChange,
            length = codeLength,
            minBoxSize = 44.dp,
            maxBoxSize = 56.dp,
            boxSpacing = 8.dp,
            boxBorderColor = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
            boxFocusedBorderColor = if (isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.primary,
            boxFilledBorderColor = if (isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        if (onResendClick != null && resendText != null) {
            TextButton(onClick = onResendClick) {
                Text(
                    text = resendText,
                    modifier = Modifier.padding(top = 16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            title = buttonText,
            onClick = onConfirmClick,
            isLoading = isLoading,
            modifier = Modifier.padding(top = 32.dp),
        )
    }
}