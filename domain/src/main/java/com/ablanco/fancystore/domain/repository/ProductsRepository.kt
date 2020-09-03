package com.ablanco.fancystore.domain.repository

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Product

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
interface ProductsRepository {

    suspend fun getProducts(): Either<List<Product>>
}
