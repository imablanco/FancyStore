package com.ablanco.fancystore.mocks

import com.ablanco.fancystore.data.models.CartMapper
import com.ablanco.fancystore.data.models.toCartProduct
import com.ablanco.fancystore.domain.models.*
import com.ablanco.fancystore.domain.transformers.DiscountTransformersImpl

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
object MockData {

    val mockProducts = listOf(
        Product(
            code = "VOUCHER",
            name = "Cabify Voucher",
            price = 5.0
        ),
        Product(
            code = "TSHIRT",
            name = "Cabify T-Shirt",
            price = 20.0
        ),
        Product(
            code = "MUG",
            name = "Cabify Coffee Mug",
            price = 7.5
        )
    )

    val mockDiscounts: List<Discount> = listOf(
        FreeItemDiscount(
            items = listOf("VOUCHER"),
            minAmount = 2,
            freeAmount = 1
        ),
        BulkDiscount(
            items = listOf("TSHIRT"),
            minAmount = 3,
            priceFactor = 0.05
        )
    )

    @Suppress("UNCHECKED_CAST")
    fun buildMockCart(products: List<Product>): Cart {
        val cartProducts = products.map(Product::toCartProduct)
        return CartMapper(discountTransformers = DiscountTransformersImpl()).map(
            cartProducts,
            mockDiscounts
        )
    }
}
