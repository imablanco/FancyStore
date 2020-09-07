package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.domain.repository.ProductsRepository

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface GetProductsUseCase {

    suspend operator fun invoke(): Either<List<Product>>
}

class GetProductsUseCaseImpl(
    private val productsRepository: ProductsRepository
) : GetProductsUseCase {

    override suspend fun invoke(): Either<List<Product>> = productsRepository.getProducts()
}
