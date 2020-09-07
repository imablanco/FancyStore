package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface GetCartItemCountUseCase {

    suspend operator fun invoke(): Flow<Either<Int>>
}

class GetCartItemCountUseCaseImpl(
    private val productsRepository: ProductsRepository
) : GetCartItemCountUseCase {

    override suspend fun invoke(): Flow<Either<Int>> = productsRepository.getCartItemCount()
}
