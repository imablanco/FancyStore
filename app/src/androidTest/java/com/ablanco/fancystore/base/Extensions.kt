package com.ablanco.fancystore.base

import android.app.Activity
import androidx.annotation.StringRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */

inline fun <reified T : Activity> launch(): ActivityScenario<T> =
    ActivityScenario.launch(T::class.java)

fun <T : PageObject> T.checkSnackBarIsDisplayed(@StringRes message: Int) = apply {
    onView(withText(message)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
}

fun <T> thatIs(matcher: Matcher<T>): Matcher<T> = `is`(matcher)

