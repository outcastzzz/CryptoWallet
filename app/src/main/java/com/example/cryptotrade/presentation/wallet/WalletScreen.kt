@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cryptotrade.presentation.wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dynamic.sdk.android.Models.BaseWallet
import com.example.cryptotrade.core.common.maskWalletAddress
import com.example.cryptotrade.presentation.base.components.PrimaryButton
import com.example.cryptotrade.presentation.wallet.state.WalletEvent
import com.example.cryptotrade.presentation.wallet.state.WalletState

@Composable
fun WalletScreen(
    state: WalletState,
    onEvent: (WalletEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    pullToRefreshState: PullToRefreshState,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Wallet",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onEvent(WalletEvent.Logout) },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        },
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.padding(16.dp),
            )
        }
    ) { padding ->
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(WalletEvent.Refresh) },
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    state.error != null -> {
                        item {
                            ErrorContent(
                                error = state.error.message.orEmpty(),
                                onRetry = { onEvent(WalletEvent.Refresh) },
                                modifier = Modifier.align(Alignment.Center),
                            )
                        }
                    }

                    else -> {
                        state.wallet?.let {
                            item {
                                WalletContent(
                                    wallet = state.wallet,
                                    balance = state.balance,
                                    network = state.network,
                                    onSendClick = { onEvent(WalletEvent.SendClick) },
                                    onLogoutClick = { onEvent(WalletEvent.Logout) },
                                    onCopyAddressClick = {
                                        onEvent(
                                            WalletEvent.CopyAddress(state.wallet.address)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorContent(error: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Error: $error",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
private fun WalletContent(
    wallet: BaseWallet,
    balance: String,
    network: String,
    onSendClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCopyAddressClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Balance",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = balance,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = wallet.address.maskWalletAddress(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                )
                Text(
                    text = "Current network: $network",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
                )
            }
        }

        PrimaryButton(
            title = "Copy Address",
            onClick = onCopyAddressClick,
            modifier = Modifier.padding(top = 32.dp),
        )

        PrimaryButton(
            title = "Send Transaction",
            onClick = onSendClick,
            modifier = Modifier.padding(top = 16.dp),
        )

        PrimaryButton(
            title = "Logout",
            onClick = onLogoutClick,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview
@Composable
fun WalletScreenWithWalletPreview() {
    MaterialTheme {
        WalletScreen(
            state = WalletState(
                isLoading = false,
                isRefreshing = false,
                error = null,
                wallet = BaseWallet(
                    address = "0x9876...5432",
                    chain = "123122211",
                    walletProvider = "Polygon",
                ),
            ),
            onEvent = {},
            snackBarHostState = remember { SnackbarHostState() },
            pullToRefreshState = rememberPullToRefreshState(),
        )
    }
}

@Preview
@Composable
fun WalletScreenErrorPreview() {
    MaterialTheme {
        WalletScreen(
            state = WalletState(
                isLoading = false,
                isRefreshing = false,
                error = Throwable("Network connection failed"),
                wallet = null
            ),
            onEvent = {},
            snackBarHostState = remember { SnackbarHostState() },
            pullToRefreshState = rememberPullToRefreshState(),
        )
    }
}

@Preview
@Composable
fun WalletScreenRefreshingPreview() {
    MaterialTheme {
        WalletScreen(
            state = WalletState(
                isLoading = false,
                isRefreshing = true,
                error = null,
                wallet = BaseWallet(
                    address = "0x9876...5432",
                    chain = "123122211",
                    walletProvider = "Polygon",
                ),
            ),
            onEvent = {},
            snackBarHostState = remember { SnackbarHostState() },
            pullToRefreshState = rememberPullToRefreshState(),
        )
    }
}

@Preview
@Composable
fun WalletScreenLargeBalancePreview() {
    MaterialTheme {
        WalletScreen(
            state = WalletState(
                isLoading = false,
                isRefreshing = false,
                error = null,
                wallet = BaseWallet(
                    address = "0x9876...5432",
                    chain = "123122211",
                    walletProvider = "Polygon",
                ),
            ),
            onEvent = {},
            snackBarHostState = remember { SnackbarHostState() },
            pullToRefreshState = rememberPullToRefreshState(),
        )
    }
}
