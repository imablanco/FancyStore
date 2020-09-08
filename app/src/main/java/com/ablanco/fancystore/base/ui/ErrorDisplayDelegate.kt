@file:Suppress("FunctionName")

package com.ablanco.fancystore.base.ui

import android.view.View
import androidx.lifecycle.Observer
import com.ablanco.fancystore.R
import com.ablanco.fancystore.presentation.ErrorDisplay
import com.ablanco.fancystore.presentation.PresentationError
import com.ablanco.fancystore.presentation.RecoverableError
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 *
 * [ErrorDisplay] implementation that uses a delegated View in which to show a [Snackbar] with
 * the error
 */
class ErrorDisplayDelegate(private val getDisplay: () -> View) : ErrorDisplay {

    override fun display(error: PresentationError) {
        val duration = when (error) {
            is RecoverableError -> Snackbar.LENGTH_INDEFINITE
            else -> Snackbar.LENGTH_SHORT
        }
        Snackbar.make(getDisplay(), error.message, duration).apply {
            if (error is RecoverableError) {
                setAction(R.string.errorActionRetry) { error.onRetry() }
            }
        }.show()
    }
}

/*Handy function to create and [Observer] that forwards errors to [ErrorDisplay]*/
fun ErrorDisplay.ErrorDisplayObserver(): Observer<PresentationError> = Observer(::display)
