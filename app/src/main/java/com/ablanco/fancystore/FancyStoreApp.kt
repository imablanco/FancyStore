package com.ablanco.fancystore

import android.app.Application
import com.ablanco.fancystore.base.ui.CurrentActivityProvider
import com.ablanco.fancystore.di.*
import org.koin.android.ext.android.get
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
                    DataResolver.modules,
                    PresentationResolver.modules,
                    AppResolver.modules
                ).flatten()
            )
        }

        /*Attach app asap*/
        get<CurrentActivityProvider>().attach(this)
    }
}
