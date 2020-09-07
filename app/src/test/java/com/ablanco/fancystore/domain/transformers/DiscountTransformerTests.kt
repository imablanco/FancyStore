package com.ablanco.fancystore.domain.transformers

import com.ablanco.fancystore.data.models.toCartProduct
import com.ablanco.fancystore.domain.models.BulkDiscount
import com.ablanco.fancystore.domain.models.CartProduct
import com.ablanco.fancystore.domain.models.FreeItemDiscount
import com.ablanco.fancystore.domain.models.Product
import org.junit.Assert
import org.junit.Test

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */

/*We could add tests for every different DiscountTransformer, DiscountValidator, etc
* but in this case just by testing all the example cases present in challenge README
* will do the work*/

private const val AssertionDelta: Double = 1e-15

class DiscountTransformerTests {

    private val mockVoucher = Product(
        code = "VOUCHER",
        name = "Cabify Voucher",
        price = 5.0
    ).toCartProduct()
    private val mockTShirt = Product(
        code = "TSHIRT",
        name = "Cabify T-Shirt",
        price = 20.0
    ).toCartProduct()
    private val mockMug = Product(
        code = "MUG",
        name = "Cabify Coffee Mug",
        price = 7.5
    ).toCartProduct()

    private val discounts = listOf(
        FreeItemDiscount(
            items = listOf("VOUCHER"),
            minAmount = 2,
            freeAmount = 1
        ), BulkDiscount(
            items = listOf("TSHIRT"),
            minAmount = 3,
            priceFactor = 0.05
        )
    )

    private val discountTransformers = DiscountTransformersImpl()

    @Test
    fun `Given VOUCHER, TSHIRT, MUG When applying discount Then price should be 32,50`() {
        testUseCase(
            products = listOf(mockVoucher, mockTShirt, mockMug),
            expectedPrice = 32.5
        )
    }

    @Test
    fun `Given VOUCHER, TSHIRT, VOUCHER When applying discount Then price should be 25,00`() {
        testUseCase(
            products = listOf(mockVoucher, mockTShirt, mockVoucher),
            expectedPrice = 25.0
        )
    }

    @Test
    fun `Given TSHIRT, TSHIRT, TSHIRT, VOUCHER, TSHIRT When applying discount Then price should be 81,00`() {
        testUseCase(
            products = listOf(mockTShirt, mockTShirt, mockTShirt, mockVoucher, mockTShirt),
            expectedPrice = 81.0
        )
    }

    @Test
    fun `Given VOUCHER, TSHIRT, VOUCHER, VOUCHER, MUG, TSHIRT, TSHIRT When applying discount Then price should be 74,5`() {
        testUseCase(
            products = listOf(
                mockVoucher,
                mockTShirt,
                mockVoucher,
                mockVoucher,
                mockMug,
                mockTShirt,
                mockTShirt
            ),
            expectedPrice = 74.5
        )
    }

    private fun testUseCase(products: List<CartProduct>, expectedPrice: Double) {
        /*When*/
        val (transformed, _) = discountTransformers.applyDiscounts(products, discounts)
        /*Then*/
        val price = transformed.sumByDouble { it.finalPrice }
        Assert.assertEquals(expectedPrice, price, AssertionDelta)
    }
}
