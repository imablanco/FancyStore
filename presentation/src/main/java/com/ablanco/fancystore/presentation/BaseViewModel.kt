package com.ablanco.fancystore.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */

/**
 * Represents persistent state of the View
 */
interface ViewState

/**
 * Represents one time actions for the View to handle
 */
interface ViewAction

/**
 * Represents user intents that triggers VM logic
 */
interface Intent

typealias Reducer<T> = T.() -> T

/**
 * Base [ViewModel] to extend. It is parametrized on [ViewState],  [ViewAction] and [Intent].
 *
 * [ViewState] mutation must be done through [BaseViewModel.setState].
 *
 * [ViewAction] dispatching must be done through [BaseViewModel.dispatchAction].
 *
 * [PresentationError] dispatching must be done through [BaseViewModel.dispatchError].
 *
 * [Intent] send must be done through [BaseViewModel.sendIntent].
 */
abstract class BaseViewModel<V : ViewState, A : ViewAction, I : Intent>(
    protected val dispatchers: CoroutinesDispatchers
) : ViewModel() {

    protected abstract val initialViewState: V

    private val _viewState: MutableLiveData<V> by lazy { MutableLiveData(initialViewState) }
    private val _viewActions: LiveEvent<A> = LiveEvent()
    private val _viewErrors: LiveEvent<PresentationError> = LiveEvent()

    val viewState: LiveData<V> by lazy { _viewState }
    val viewActions: LiveData<A> by lazy { _viewActions }
    val viewErrors: LiveData<PresentationError> by lazy { _viewErrors }

    abstract fun load()

    protected abstract fun handleIntent(intent: I)

    /**
     * Returns the current state of the View. Its is safe to call
     * as it guaranteed to always return a non null instance of [ViewState]
     */
    fun getState(): V = viewState.value ?: throw IllegalStateException()

    /**
     * Handles the given [Intent] sent by the View
     */
    fun sendIntent(intent: I) = handleIntent(intent)

    /**
     * Mutates the current [ViewState] via [reduce] function and notify
     * [viewState] observers
     */
    protected fun setState(reduce: Reducer<V>) {
        _viewState.value = reduce(getState())
    }

    /**
     * Dispatches the given [ViewAction] to [viewActions] observers
     */
    protected fun dispatchAction(action: A) {
        _viewActions.value = action
    }

    /**
     * Dispatches the given [PresentationError] to [viewErrors] observers
     */
    protected fun dispatchError(error: PresentationError) {
        _viewErrors.value = error
    }

    /**
     * Launches the given suspend block in the scope of this ViewModel
     */
    protected fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch(block = block)
}

