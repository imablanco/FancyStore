package com.ablanco.fancystore.presentation

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 *
 * Contract for classes that knows how to show [PresentationError]
 */
interface ErrorDisplay {

    fun display(error: PresentationError)
}
