package com.ablanco.fancystore.base.presentation

import com.ablanco.fancystore.base.ui.CurrentActivityProvider

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 *
 * Abstraction to provide Android strings resources
 */
interface StringsProvider {

    operator fun invoke(stringResId: Int, vararg format: Any): String
}

/**
 * Implementation of [StringsProvider] that gets String resources from Android Context.
 * Its very important to mention that using [CurrentActivityProvider] allows this class to
 * work consistently across configuration changes (i.e App locale changes) as the current visible
 * [android.app.Activity] is gathered every time [invoke] is called.
 */
class StringsProviderImpl(private val activityProvider: CurrentActivityProvider) : StringsProvider {

    override fun invoke(stringResId: Int, vararg format: Any): String =
        activityProvider.activity?.getString(stringResId, *format).orEmpty()
}
