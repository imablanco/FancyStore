package com.ablanco.fancystore.domain.repository

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Cart
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import kotlinx.coroutines.flow.Flow

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
interface ProductsRepository {

    suspend fun getProducts(): Either<List<Product>>

    suspend fun getProductDiscounts(): Either<List<Discount>>

    suspend fun saveProductToCart(productId: String): Either<Unit>

    /*By returning a [Flow] here we guarantee that every component is always be up to date
    * by subscribing to us*/
    suspend fun getCartItemCount(): Flow<Either<Int>>

    suspend fun getCart(): Flow<Either<Cart>>
}
