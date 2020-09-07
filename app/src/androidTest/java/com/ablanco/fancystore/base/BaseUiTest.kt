package com.ablanco.fancystore.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import com.karumi.shot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.core.context.stopKoin

/**
 * Created by Álvaro Blanco Cabrero on 07/09/2020.
 * FancyStore.
 */
abstract class BaseUiTest : ScreenshotTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val dispatchers: CoroutinesDispatchers = TestCoroutinesDispatchers(
        coroutinesTestRule.testDispatcher
    )

    val stringsProvider: StringsProvider
        get() = object : StringsProvider {
            override fun invoke(stringResId: Int, vararg format: Any): String =
                InstrumentationRegistry.getInstrumentation().targetContext.getString(
                    stringResId,
                    *format
                )
        }

    @Before
    @After
    fun clearKoin() {
        stopKoin()
    }

    @Before
    fun setUpIntents() {
        Intents.init()
    }

    @After
    fun tearDownIntents() {
        Intents.release()
    }
}
