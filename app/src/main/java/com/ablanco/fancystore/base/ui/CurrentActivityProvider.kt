package com.ablanco.fancystore.base.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 *
 * Provides the current showing [Activity] across the app to allow components that need an
 * Activity [android.content.Context] to rely on this without requiring an explicit dependency
 * on the [Activity]
 */
interface CurrentActivityProvider {

    val activity: Activity?

    fun attach(application: Application)
}

class CurrentActivityProviderImpl : CurrentActivityProvider,
    Application.ActivityLifecycleCallbacks {

    private var activityRef = WeakReference<Activity>(null)

    override val activity: Activity?
        get() = activityRef.get()

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivityStarted(activity: Activity) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityRef = WeakReference(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    override fun attach(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }
}

