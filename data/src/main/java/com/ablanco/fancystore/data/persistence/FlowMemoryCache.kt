package com.ablanco.fancystore.data.persistence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.channels.BroadcastChannel as Subject
import kotlinx.coroutines.channels.ConflatedBroadcastChannel as BehaviorSubject

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 *
 * Class that contains helper methods to deal with live memory based cache backed with [Flow].
 * [Key] type MUST implement [equals] and [hashCode] in order for cache to work properly
 *
 * This class works backed by a [MemoryCache].
 */
class FlowMemoryCache<Key, Value : Any> {

    /*Channel that emits on every cache change for [getAll] to works*/
    private val allChangeSignal: Subject<Unit> = BehaviorSubject(Unit)

    /*Every different key is backed by a channel that will emit a dummy value whenever
    * the [MemoryCache] within it changes*/
    private val cacheChangeSignals: MutableMap<Key, BehaviorSubject<Unit>> = mutableMapOf()

    private val cache: MemoryCache<Key, Value> = MemoryCache()

    operator fun get(key: Key): Flow<Value?> = channelForKey(key)
        .asFlow()
        .map { cache[key].rightOrNull() }

    operator fun set(key: Key, value: Value) {
        /*1 - Update the underlying cache
        * 2 - Send the value to the channel
        * 3 - Update [allChangeSignal] for subscriptions in [getAll] fun*/
        cache[key] = value
        channelForKey(key).offer(Unit)
        allChangeSignal.offer(Unit)
    }

    fun getAll(): Flow<List<Value>> = allChangeSignal
        .asFlow()
        .map { cache.getAll().rightOrNull().orEmpty() }

    fun remove(key: Key) {
        /*Same steps as in [set]*/
        cache.remove(key)
        channelForKey(key).offer(Unit)
        allChangeSignal.offer(Unit)
    }

    fun clear() {
        /*Same steps as in [set]*/
        cache.clear()
        cacheChangeSignals.keys.forEach { key -> channelForKey(key).offer(Unit) }
        allChangeSignal.offer(Unit)
    }

    /*Returns the channel for the given key or creates a new one*/
    private fun channelForKey(key: Key): BehaviorSubject<Unit> =
        cacheChangeSignals.getOrPut(key) { BehaviorSubject(Unit) }
}
