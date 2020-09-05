package com.ablanco.fancystore.di

import com.ablanco.fancystore.data.network.ProductsApiDataSource
import com.ablanco.fancystore.data.network.ProductsService
import com.ablanco.fancystore.data.network.ServiceBuilder
import com.ablanco.fancystore.data.persistence.ProductsMemoryDataSource
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
object DataResolver : DependencyResolver {

    override val modules: List<Module> get() = listOf(networkModule, persistenceModule)

    private val networkModule = module {
        /*Services*/
        single { ServiceBuilder(getProperty(DiProperties.ApiBaseUrl)) }
        factory<ProductsService> { get<ServiceBuilder>().create() }

        /*DataSources*/
        factory { ProductsApiDataSource(get()) }
    }

    private val persistenceModule = module {
        /*DataSources*/
        factory { ProductsMemoryDataSource() }
    }
}
