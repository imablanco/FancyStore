package com.ablanco.fancystore.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.OngoingStubbing
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.hamcrest.Matcher

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
fun OngoingStubbing.respondWithOk() = respondWith(
    Instrumentation.ActivityResult(Activity.RESULT_OK, null)
)

inline fun <reified T> withComponent(): Matcher<Intent> =
    IntentMatchers.hasComponent(T::class.java.name)
