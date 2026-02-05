package com.example.cryptotrade.core.di

import android.content.Context
import com.example.cryptotrade.data.repository.AuthRepositoryImpl
import com.example.cryptotrade.data.repository.WalletRepositoryImpl
import com.example.cryptotrade.domain.clipboard.ClipboardManager
import com.example.cryptotrade.domain.repository.AuthRepository
import com.example.cryptotrade.domain.repository.WalletRepository
import com.example.cryptotrade.presentation.base.AndroidClipboardManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object {

        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context = context
    }

    @Binds
    @Singleton
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindWalletRepository(impl: WalletRepositoryImpl): WalletRepository

    @Binds
    fun bindClipboardManager(impl: AndroidClipboardManager): ClipboardManager
}
