package com.ablanco.fancystore.base

import android.app.Activity
import androidx.test.core.app.ActivityScenario

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 *
 * Base implementation for Page Objects
 */
interface PageObject {

    fun start(): PageObject
}

abstract class ActivityPageObject<T : Activity> : PageObject {

    abstract val activityClass: Class<T>

    final override fun start(): PageObject = apply { ActivityScenario.launch(activityClass) }
}
