package com.ablanco.fancystore.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.ablanco.fancystore.presentation.ErrorDisplay
import com.ablanco.fancystore.presentation.PresentationError
import com.ablanco.fancystore.utils.ui.viewBinding

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(), WithBinding<V>, ErrorDisplay {

    protected abstract val getBinding: (LayoutInflater) -> V

    override val binding: V by viewBinding { getBinding(layoutInflater) }

    private val errorDisplayDelegate = ErrorDisplayDelegate { binding.root }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun display(error: PresentationError) = errorDisplayDelegate.display(error)
}
