package com.ablanco.fancystore.data.repositories

import com.ablanco.fancystore.data.network.ProductsApiDataSource
import com.ablanco.fancystore.data.persistence.ProductsMemoryDataSource
import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.GenericError
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.models.Discount
import com.ablanco.fancystore.domain.models.Product
import com.ablanco.fancystore.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
class ProductsRepositoryImpl(
    private val apiDataSource: ProductsApiDataSource,
    private val cacheDataSource: ProductsMemoryDataSource
) : ProductsRepository {

    override suspend fun getProducts(): Either<List<Product>> =
        cacheDataSource.getProducts().orDefault {
            apiDataSource.getProducts().doOnRight(cacheDataSource::saveProducts)
        }

    override suspend fun getProductDiscounts(): Either<List<Discount>> =
        cacheDataSource.getDiscounts().orDefault {
            apiDataSource.getProductDiscounts().doOnRight(cacheDataSource::saveDiscounts)
        }

    override suspend fun saveProductToCart(productId: String): Either<Unit> {
        /*In a real app this should call to back and get either the saved product back in the response
        * or return the updated Cart so we can update the cache properly with up to date data*/

        val product = cacheDataSource.getProduct(productId).rightOrNull()
            ?: return Left(GenericError)
        cacheDataSource.saveProductToCart(product)
        return Right(Unit)
    }

    /*Returning [Either] here is and useless as it always return Right instances, but in a real app
    * this method would call back with all its implications*/
    override suspend fun getCartItemCount(): Flow<Either<Int>> =
        cacheDataSource.getCart().map { Right(it.size) }
}
