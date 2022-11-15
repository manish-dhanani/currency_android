package com.app.currency.di

import android.content.Context
import com.app.currency.util.NetworkConnection
import com.app.currency.util.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The Dagger Module provides instance of utility classes.
 */
@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkConnection =
        NetworkConnection(context)

    @Singleton
    @Provides
    fun providePrefManager(@ApplicationContext context: Context): PrefManager = PrefManager(context)

}