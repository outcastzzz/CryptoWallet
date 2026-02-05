package com.example.cryptotrade.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cryptotrade.presentation.navigation.routes.LoginScreenRoute
import com.example.cryptotrade.presentation.navigation.routes.SendScreenRoute
import com.example.cryptotrade.presentation.navigation.routes.SplashScreenRoute
import com.example.cryptotrade.presentation.navigation.routes.WalletScreenRoute

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Wallet : Screen("wallet")
    data object Send : Screen("send")
    data object Splash : Screen("splash")
}

@Composable
fun CryptoTradeNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        splashScreen(navController)
        walletScreen(navController)
        sendScreen(navController)
        loginScreen(navController)
    }
}

private fun NavGraphBuilder.splashScreen(navController: NavHostController) {
    composable(Screen.Splash.route) {
        SplashScreenRoute(
            onNavigateToWallet = {
                navController.navigate(Screen.Wallet.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            },
            onNavigateToEmail = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        )
    }
}

private fun NavGraphBuilder.walletScreen(navController: NavHostController) {
    composable(Screen.Wallet.route) {
        WalletScreenRoute(
            onNavigateToSend = { navController.navigate(Screen.Send.route) },
            onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Wallet.route) { inclusive = true }
                }
            }
        )
    }
}

private fun NavGraphBuilder.sendScreen(navController: NavHostController) {
    composable(Screen.Send.route) {
        SendScreenRoute(
            onNavigateBack = { navController.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.loginScreen(navController: NavHostController) {
    composable(Screen.Login.route) {
        LoginScreenRoute(
            onNavigateToWallet = {
                navController.navigate(Screen.Wallet.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        )
    }
}