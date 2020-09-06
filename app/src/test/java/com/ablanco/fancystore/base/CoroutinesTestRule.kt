package com.ablanco.fancystore.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 *
 * Test rule that changes [Dispatchers.Main] with [TestCoroutineDispatcher].
 * This will allow to execute tests synchronously
 */
class CoroutinesTestRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        testDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}

