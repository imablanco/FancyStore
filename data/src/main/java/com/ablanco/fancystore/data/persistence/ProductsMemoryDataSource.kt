package com.ablanco.fancystore.data.persistence

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 */
class ProductsMemoryDataSource {

    private val productsCache: MemoryCache<String, Product> = MemoryCache()
    private val discountsCache: MemoryCache<Unit, List<Discount>> = MemoryCache()
    private val cartCache: FlowMemoryCache<Unit, List<Product>> = FlowMemoryCache()

    fun getProduct(productId: String): Either<Product> = productsCache.get(productId)

    fun getProducts(): Either<List<Product>> = productsCache.getAll()

    fun saveProduct(product: Product) = productsCache.set(product.code, product)

    fun saveProducts(products: List<Product>) = products.forEach(::saveProduct)

    fun getDiscounts(): Either<List<Discount>> = discountsCache[Unit]

    fun saveDiscounts(discounts: List<Discount>) = discountsCache.set(Unit, discounts)

    fun getCart(): Flow<List<Product>> = cartCache[Unit].map { it.orEmpty() }

    suspend fun saveProductToCart(product: Product) {
        val products = cartCache[Unit].first().orEmpty()
        cartCache[Unit] = products + product
    }
}
