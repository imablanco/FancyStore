package com.ablanco.fancystore.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ablanco.fancystore.base.presentation.StringsProvider
import com.ablanco.fancystore.domain.base.CoroutinesDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Rule
import org.mockito.Mockito

/**
 * Created by Álvaro Blanco Cabrero on 06/09/2020.
 * FancyStore.
 */
abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    val dispatchers: CoroutinesDispatchers = TestCoroutinesDispatchers(
        coroutinesTestRule.testDispatcher
    )

    val stringsProvider: StringsProvider = FakeStringsProvider()

    @After
    fun validate() {
        /*Ensure misusing errors are showing in the correct tests and not at the end of the
        execution chain*/
        Mockito.validateMockitoUsage()
    }
}

class TestCoroutinesDispatchers(
    testCoroutineDispatcher: TestCoroutineDispatcher
) : CoroutinesDispatchers {
    override val Main: CoroutineDispatcher = testCoroutineDispatcher
    override val IO: CoroutineDispatcher = testCoroutineDispatcher
    override val Default: CoroutineDispatcher = testCoroutineDispatcher
    override val Unconfined: CoroutineDispatcher = testCoroutineDispatcher
}

class FakeStringsProvider : StringsProvider {
    override fun invoke(stringResId: Int, vararg format: Any): String = ""
}
