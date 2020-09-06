package com.ablanco.fancystore.domain.transformers

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
interface DiscountValidators {

    val validators: List<DiscountValidator<*>>
}

class DiscountValidatorsImpl : DiscountValidators {

    override val validators: List<DiscountValidator<*>> = listOf(
        ItemsPromoDiscountValidator()
    )
}
