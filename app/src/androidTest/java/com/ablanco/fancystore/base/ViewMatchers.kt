package com.ablanco.fancystore.base

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
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

internal class RecyclerViewHasItemsMatcher : TypeSafeMatcher<RecyclerView>() {

    override fun describeTo(description: Description) {
        description.appendText("has items")
    }

    public override fun matchesSafely(view: RecyclerView): Boolean {
        return view.adapter?.itemCount != 0
    }
}

class ClickViewHolderView(@IdRes private val id: Int) : ViewAction {

    override fun getConstraints(): Matcher<View> = ViewMatchers.isDisplayed()

    override fun getDescription(): String = "ClickViewHolderView"

    override fun perform(uiController: UiController?, view: View?) {
        view?.findViewById<View>(id)?.performClick()
    }
}

fun isEnabled(isEnabled: Boolean): Matcher<View> = IsEnabledMatcher(isEnabled)

@Suppress("UNCHECKED_CAST")
fun hasItems(): Matcher<View> = RecyclerViewHasItemsMatcher() as Matcher<View>

fun clickViewHolderView(@IdRes id: Int) = ClickViewHolderView(id)

