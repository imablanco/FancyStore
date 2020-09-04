package com.ablanco.fancystore.data.repositories

import com.ablanco.fancystore.data.network.ProductsApiDataSource
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.domain.repository.ProductsRepository

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
class ProductsRepositoryImpl(
    private val productsApiDataSource: ProductsApiDataSource
) : ProductsRepository {

    override suspend fun getProducts(): Either<List<Product>> =
        productsApiDataSource.getProducts()

    override suspend fun getProductDiscounts(): Either<List<Discount>> =
        productsApiDataSource.getProductDiscounts()
}
