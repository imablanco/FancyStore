package com.ablanco.fancystore.di

import com.ablanco.fancystore.data.repositories.ProductsRepositoryImpl
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.ablanco.fancystore.domain.base.CoroutinesDispatchersImpl
import com.ablanco.fancystore.domain.repository.ProductsRepository
import com.ablanco.fancystore.domain.usecases.GetProductsUseCase
import com.ablanco.fancystore.domain.usecases.GetProductsUseCaseImpl
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
object DomainResolver : DependencyResolver {

    override val modules: List<Module>
        get() = listOf(
            coreModule,
            repositoriesModule,
            useCasesModule
        )

    private val coreModule = module {
        single<CoroutinesDispatchers> { CoroutinesDispatchersImpl() }
    }

    private val repositoriesModule = module {
        single<ProductsRepository> { ProductsRepositoryImpl(get()) }
    }

    private val useCasesModule = module {
        factory<GetProductsUseCase> { GetProductsUseCaseImpl(get()) }
    }
}
