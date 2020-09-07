package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.repository.ProductsRepository

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface AddProductToCartUseCase {

    suspend operator fun invoke(productId: String): Either<Unit>
}

class AddProductToCartUseCaseImpl(
    private val productsRepository: ProductsRepository
) : AddProductToCartUseCase {

    override suspend fun invoke(productId: String): Either<Unit> =
        productsRepository.saveProductToCart(productId)
}
