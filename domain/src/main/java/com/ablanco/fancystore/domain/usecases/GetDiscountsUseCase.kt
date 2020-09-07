package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.repository.ProductsRepository

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface GetDiscountsUseCase {

    suspend operator fun invoke(): Either<List<Discount>>
}

class GetDiscountsUseCaseImpl(
    private val productsRepository: ProductsRepository
) : GetDiscountsUseCase {

    override suspend fun invoke(): Either<List<Discount>> =
        productsRepository.getProductDiscounts()
}
