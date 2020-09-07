package com.ablanco.fancystore.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.Intents
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.test.AutoCloseKoinTest

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
abstract class BaseUiTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val dispatchers: CoroutinesDispatchers = TestCoroutinesDispatchers(
        coroutinesTestRule.testDispatcher
    )

    val stringsProvider: StringsProvider = FakeStringsProvider()

    @Before
    fun setUpIntents() {
        Intents.init()
    }

    @After
    fun tearDownIntents() {
        Intents.release()
    }
}
