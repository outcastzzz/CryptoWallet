package com.example.cryptotrade.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dynamic.sdk.android.UI.DynamicUI
import com.example.cryptotrade.data.web3.DynamicSdkWrapper
import com.example.cryptotrade.presentation.navigation.CryptoTradeNavHost
import com.example.cryptotrade.presentation.theme.CryptoTradeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sdkWrapper: DynamicSdkWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdkWrapper.initialize(applicationContext, this)

        enableEdgeToEdge()
        setContent {
            CryptoTradeTheme {
                val isInitialized by sdkWrapper.isInitialized.collectAsStateWithLifecycle()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(modifier = Modifier.fillMaxSize())
                    if (isInitialized) {
                        CryptoTradeNavHost()
                    } else {
                        CircularProgressIndicator()
                    }

//                    DynamicUI()
                }
            }
        }
    }
}