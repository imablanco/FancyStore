package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Cart
import com.ablanco.fancystore.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface GetCartUseCase {

    suspend operator fun invoke(): Flow<Either<Cart>>
}

class GetCartUseCaseImpl(private val productsRepository: ProductsRepository) : GetCartUseCase {

    override suspend fun invoke(): Flow<Either<Cart>> =
        productsRepository.getCart()
}
