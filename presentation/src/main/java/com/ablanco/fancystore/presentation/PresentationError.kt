package com.ablanco.fancystore.presentation

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 *
 * Base contract for presentation errors
 */
interface PresentationError {
    val message: String
}

/*Default presentation error that is only mean to be displayed*/
class DefaultError(override val message: String) : PresentationError

/*Error that can be recovered via [onRetry] block*/
class RecoverableError(override val message: String, val onRetry: () -> Unit) : PresentationError
