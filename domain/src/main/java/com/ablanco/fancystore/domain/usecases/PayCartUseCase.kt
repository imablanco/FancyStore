package com.ablanco.fancystore.domain.usecases

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.repository.ProductsRepository

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
interface PayCartUseCase {

    suspend operator fun invoke(): Either<Unit>
}

class PayCartUseCaseImpl(private val productsRepository: ProductsRepository) : PayCartUseCase {

    override suspend fun invoke(): Either<Unit> = productsRepository.payCart()
}
