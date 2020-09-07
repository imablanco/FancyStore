package com.ablanco.fancystore.features.products.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ablanco.fancystore.R
import com.ablanco.fancystore.base.*
import com.ablanco.fancystore.features.checkout.ui.CheckoutActivity
import org.hamcrest.CoreMatchers.not

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
class ProductsPageObject : PageObject {

    fun launch() = apply {
        launch<ProductsActivity>()
        intending(withComponent<CheckoutActivity>()).respondWithOk()
    }

    fun checkCartBadgeIsGone() = apply {
        onView(withId(R.id.tvCartItemCount)).check(matches(not(isDisplayed())))
    }

    fun checkCartBadgeIsVisible() = apply {
        onView(withId(R.id.tvCartItemCount)).check(matches(isDisplayed()))
    }

    fun checkListIsEmpty() = apply {
        onView(withId(R.id.rvProducts)).check(matches(not(hasItems())))
    }

    fun checkListIsNotEmpty() = apply {
        onView(withId(R.id.rvProducts)).check(matches(hasItems()))
    }

    fun clickCart() = apply {
        onView(withChild(withId(R.id.tvCartItemCount))).perform(click())
    }

    fun clickAddToProduct() = apply {
        onView(withId(R.id.rvProducts)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductsAdapter.ProductViewHolder>(
                0,
                clickViewHolderView(R.id.ivAddToBag)
            )
        )
    }

    fun checkNavigatedToCheckout() = apply {
        intended(withComponent<CheckoutActivity>())
    }
}
