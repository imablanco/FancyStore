package com.ablanco.fancystore.data.network

import com.ablanco.fancystore.data.models.ProductListData
import com.ablanco.fancystore.domain.base.Either
import retrofit2.http.GET

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
interface ProductsService {

    @GET("palcalde/6c19259bd32dd6aafa327fa557859c2f/raw/ba51779474a150ee4367cda4f4ffacdcca479887/Products.json")
    suspend fun getProducts(): Either<ProductListData>
}
