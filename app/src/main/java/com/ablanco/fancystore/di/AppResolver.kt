package com.ablanco.fancystore.di

import com.ablanco.fancystore.base.ui.CurrentActivityProvider
import com.ablanco.fancystore.base.ui.CurrentActivityProviderImpl
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
object AppResolver : DependencyResolver {

    override val modules: List<Module> get() = listOf(appModule)

    private val appModule = module {
        single<CurrentActivityProvider> { CurrentActivityProviderImpl() }
    }
}
