package com.ablanco.fancystore.data.network

import com.ablanco.fancystore.data.models.DiscountData
import com.ablanco.fancystore.data.models.ItemsPromoDiscountData
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 *
 * As [DiscountData] is a super type we need to rely on a custom [JsonDeserializer] to deserialize
 * to the correct type.
 */
private const val PropertyType = "type"

class ProductDiscountTypeAdapter : JsonDeserializer<DiscountData> {

    /*These is the list of deserializers for the known types.
    * Handling a new discount is as easy as adding a new deserializers to the list*/
    private val deserializers = listOf(
        createDiscountDeserializer<ItemsPromoDiscountData>(DiscountType.Promotion)
    )

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DiscountData? {

        val type = json.asJsonObject[PropertyType].asString ?: return null
        return deserializers.find { it.type.serializedName == type }?.deserialize(json, context)
    }
}

enum class DiscountType(val serializedName: String) {
    Promotion("promotion")
}

/**
 * Just an utility to allow discount deserialize to scale as new discounts are introduced
 */
interface DiscountDeserializer {

    val type: DiscountType

    fun deserialize(json: JsonElement, context: JsonDeserializationContext): DiscountData
}

/*With the power of reified we can have a generic function to map every different discount
* type with its class representation*/
inline fun <reified T> createDiscountDeserializer(type: DiscountType) =
    object : DiscountDeserializer {
        override val type: DiscountType = type

        override fun deserialize(
            json: JsonElement,
            context: JsonDeserializationContext
        ): DiscountData = context.deserialize(json, T::class.java)
    }
