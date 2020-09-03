package com.ablanco.fancystore.data.network

import com.ablanco.fancystore.data.models.ProductListData
import com.ablanco.fancystore.data.models.toDomain
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Product

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
class ProductsApiDataSource(
    private val productsService: ProductsService
) {

    suspend fun getProducts(): Either<List<Product>> =
        productsService.getProducts().map(ProductListData::toDomain)
}
