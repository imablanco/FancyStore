package com.ablanco.fancystore.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(),
    WithBinding<V> {

    protected abstract val getBinding: (LayoutInflater) -> V

    override val binding: V by viewBinding { getBinding(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
