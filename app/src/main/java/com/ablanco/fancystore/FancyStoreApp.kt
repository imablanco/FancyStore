package com.ablanco.fancystore

import android.app.Application
import com.ablanco.fancystore.di.DataResolver
import com.ablanco.fancystore.di.DiProperties
import com.ablanco.fancystore.di.DomainResolver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
class FancyStoreApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FancyStoreApp)
            properties(
                mapOf(
                    DiProperties.ApiBaseUrl to BuildConfig.BaseUrl
                )
            )
            modules(
                listOf(
                    DomainResolver.modules,
                    DataResolver.modules
                ).flatten()
            )
        }
    }
}
