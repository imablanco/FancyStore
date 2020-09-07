package com.ablanco.fancystore.base

import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
class TestCoroutinesDispatchers(
    testCoroutineDispatcher: TestCoroutineDispatcher
) : CoroutinesDispatchers {
    override val Main: CoroutineDispatcher = testCoroutineDispatcher
    override val IO: CoroutineDispatcher = testCoroutineDispatcher
    override val Default: CoroutineDispatcher = testCoroutineDispatcher
    override val Unconfined: CoroutineDispatcher = testCoroutineDispatcher
}
