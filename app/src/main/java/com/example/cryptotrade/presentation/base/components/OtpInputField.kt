package com.example.cryptotrade.presentation.base.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpInputField(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    boxAspectRatio: Float = 1f,
    minBoxSize: Dp = 40.dp,
    maxBoxSize: Dp = 64.dp,
    boxSpacing: Dp = 8.dp,
    boxBackgroundColor: Color = Color.White,
    boxBorderColor: Color = Color.LightGray,
    boxFocusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    boxFilledBorderColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = TextStyle(
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground,
    ),
    cornerRadius: Dp = 8.dp,
) {
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current

    var containerWidth by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val calculatedBoxSize = remember(containerWidth, length, boxSpacing) {
        if (containerWidth == 0) return@remember minBoxSize

        with(density) {
            val spacingPx = boxSpacing.toPx()
            val totalSpacing = spacingPx * (length - 1)
            val availableWidth = containerWidth - totalSpacing
            val boxSizePx = availableWidth / length

            // Ограничиваем размер между min и max
            boxSizePx.toDp().coerceIn(minBoxSize, maxBoxSize)
        }
    }

    // Размер текста пропорционален размеру квадратика
    val fontSize = with(density) {
        (calculatedBoxSize.toPx() * 0.45f).toSp()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        BasicTextField(
            value = code,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() }.take(length)
                onCodeChange(filtered)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .size(1.dp)
                .alpha(0f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword
            ),
            decorationBox = {},
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(boxSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(length) { index ->
                val char = code.getOrNull(index)?.toString() ?: ""
                val isFocused = code.length == index
                val isFilled = char.isNotEmpty()

                val borderColor = when {
                    isFilled -> boxFilledBorderColor
                    isFocused -> boxFocusedBorderColor
                    else -> boxBorderColor
                }

                Box(
                    modifier = Modifier
                        .size(
                            width = calculatedBoxSize,
                            height = calculatedBoxSize * boxAspectRatio,
                        )
                        .clip(RoundedCornerShape(cornerRadius))
                        .background(boxBackgroundColor)
                        .border(
                            width = if (isFocused || isFilled) 2.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .noRippleClickable {
                            focusRequester.requestFocus()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = char,
                        style = textStyle,
                    )

                    if (isFocused) {
                        val cursorHeight = calculatedBoxSize * 0.5f
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(cursorHeight)
                                .background(boxFocusedBorderColor),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = this.then(
    Modifier.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )
)

@Preview(showBackground = true, name = "All States")
@Composable
private fun OtpInputFieldAllStatesPreview(
    @PreviewParameter(OtpCodePreviewProvider::class) code: String
) {
    MaterialTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Code: \"$code\"",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                OtpInputField(
                    code = code,
                    onCodeChange = {}
                )
            }
        }
    }
}

class OtpCodePreviewProvider : PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        "",
        "1",
        "12",
        "123",
        "1234",
        "12345",
        "123456"
    )
}

