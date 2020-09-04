package com.ablanco.fancystore.base.presentation

import kotlinx.coroutines.Deferred

/**
 * Created by Álvaro Blanco Cabrero on 04/09/2020.
 * FancyStore.
 */
@Suppress("UNCHECKED_CAST")
suspend fun <A, B, C> zip(
    def1: Deferred<A>,
    def2: Deferred<B>,
    biFunction: (A, B) -> C
): C = biFunction(def1.await(), def2.await())

suspend fun <A, B> zipPair(
    def1: Deferred<A>,
    def2: Deferred<B>
): Pair<A, B> = zip(def1, def2) { d1, d2 -> d1 to d2 }
