package com.ablanco.fancystore.di

import org.koin.core.module.Module

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 *
 * Simple contract for classes that wants to participate into DI
 */
interface DependencyResolver {

    val modules: List<Module>
}
