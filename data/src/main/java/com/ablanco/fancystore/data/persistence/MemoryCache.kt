package com.ablanco.fancystore.data.persistence

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right

/**
 * Created by Álvaro Blanco Cabrero on 05/09/2020.
 * FancyStore.
 *
 * Class that contains helper methods to deal with memory based cache.
 * [Key] type MUST implement [equals] and [hashCode] in order for cache to work properly
 *
 */
class MemoryCache<Key, Value : Any> {

    private val cache: MutableMap<Key, Value> = mutableMapOf()

    operator fun get(key: Key): Either<Value> = cache[key]?.let { Right(it) } ?: Left(CacheNotFoundException)

    operator fun set(key: Key, value: Value) {
        cache[key] = value
    }

    fun getAll(): Either<List<Value>> {
        val values = cache.keys.map(::get).mapNotNull { it.rightOrNull() }
        return if (values.isNotEmpty()) Right(values) else Left(EmptyCacheException)
    }

    fun remove(key: Key) {
        cache.remove(key)
    }

    fun clear() = cache.clear()
}

object CacheNotFoundException : Exception()
object EmptyCacheException : Exception()
