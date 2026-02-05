package com.example.cryptotrade.data.web3

import android.app.Activity
import android.content.Context
import com.dynamic.sdk.android.Chains.EVM.EthereumTransaction
import com.dynamic.sdk.android.DynamicSDK
import com.dynamic.sdk.android.Models.BaseWallet
import com.dynamic.sdk.android.Models.Network
import com.dynamic.sdk.android.core.ClientProps
import com.dynamic.sdk.android.core.LoggerLevel
import com.example.cryptotrade.BuildConfig
import com.example.cryptotrade.core.common.Constants
import com.example.cryptotrade.core.common.EthUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicSdkWrapper @Inject constructor() {

    private var _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private var cachedWallet: BaseWallet? = null

    private val sdk: DynamicSDK
        get() {
            check(_isInitialized.value) { "SDK not initialized. Call initialize() first." }
            return DynamicSDK.getInstance()
        }

    fun initialize(context: Context, activity: Activity) {
        if (_isInitialized.value) return

        val props = ClientProps(
            environmentId = BuildConfig.DYNAMIC_ENV_ID,
            appName = "namee",
            logLevel = if (BuildConfig.DEBUG) LoggerLevel.DEBUG else LoggerLevel.ERROR,
        )

        DynamicSDK.initialize(props, context, activity)
        _isInitialized.value = true
    }

    val token: String
        get() = sdk.auth.token.orEmpty()

    val readyChanges: Flow<Boolean>
        get() = sdk.sdk.readyChanges

    suspend fun logout() {
        sdk.auth.logout()
    }

    suspend fun sendOtp(email: String) = sdk.auth.email.sendOTP(email)

    suspend fun verifyOtp(code: String) = sdk.auth.email.verifyOTP(token = code)

    fun getEvmWallet(): BaseWallet? {
        return cachedWallet ?: run {
            val wallets = sdk.wallets.userWallets

            val evmWallet = wallets.firstOrNull { wallet ->
                wallet.chain.equals(Constants.EVM_CHAIN_UPPERCASE, ignoreCase = true)
            }

            val resultWallet = evmWallet ?: sdk.wallets.primary
            cachedWallet = resultWallet
            resultWallet
        }
    }

    suspend fun getNetwork(): String? {
        return cachedWallet?.let { wallet ->
            val json = sdk.wallets.getNetwork(wallet = wallet).value

            if (json is JsonPrimitive) {
                json.intOrNull?.toString() ?: json.contentOrNull ?: json.toString()
            } else {
                json.toString()
            }
        }
    }

    suspend fun switchTestnet() {
        cachedWallet?.let { wallet ->
            sdk.wallets.switchNetwork(
                wallet = wallet,
                network = Network.evm(Constants.SEPOLIA_CHAIN_ID.toInt()),
            )
        }
    }

    suspend fun getBalance(): String {
        return cachedWallet?.let {
            sdk.wallets.getBalance(wallet = it)
        }.orEmpty()
    }

    suspend fun sendTransaction(to: String, amountEth: BigInteger): String {
        val wallet = cachedWallet ?: getEvmWallet()

        val value = EthUtils.ethToWei(amountEth)

        val transaction = EthereumTransaction(
            from = wallet?.address.orEmpty(),
            to = to,
            value = value,
            gas = BigInteger.valueOf(21000),
        )

        val txHash = wallet?.let {
            sdk.evm.sendTransaction(
                wallet = it,
                transaction = transaction
            )
        }

        return txHash.orEmpty()
    }
}

data class WalletData(
    val id: String,
    val walletName: String,
    val walletProvider: String,
    val publicKey: String,
    val address: String,
    val chain: String,
)