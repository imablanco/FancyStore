package com.ablanco.fancystore.di

import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.base.presentation.StringsProviderImpl
import com.ablanco.fancystore.features.products.presentation.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
object PresentationResolver : DependencyResolver {

    override val modules: List<Module> get() = listOf(viewModelsModule, miscModule)

    private val viewModelsModule = module {

        viewModel { ProductsViewModel(get(), get(), get(), get()) }
    }

    private val miscModule = module {
        single<StringsProvider> { StringsProviderImpl(get()) }
    }
}
