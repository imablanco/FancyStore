package com.ablanco.fancystore.base

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
internal class IsEnabledMatcher(private val isEnabled: Boolean) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("is enabled: $isEnabled")
    }

    public override fun matchesSafely(view: View): Boolean {
        return view.isEnabled == isEnabled
    }
}


fun isEnabled(isEnabled: Boolean): Matcher<View> = IsEnabledMatcher(isEnabled)
