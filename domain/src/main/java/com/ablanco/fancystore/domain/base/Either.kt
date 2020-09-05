package com.ablanco.fancystore.domain.base

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 *
 * A right-biased class that represents successful values in the right side and errors in the left.
 */
sealed class Either<A : Any> {

    fun rightOrNull(): A? = (this as? Right<A>)?.value
    fun leftOrNull(): Throwable? = (this as? Left<A>)?.error

    inline fun <B : Any> map(mapper: (A) -> B): Either<B> =
        when (this) {
            is Right -> Right(
                mapper(value)
            )
            is Left -> Left(
                error
            )
        }

    inline fun <B : Any> flatMap(mapper: (A) -> Either<B>): Either<B> =
        when (this) {
            is Right -> mapper(value)
            is Left -> Left(
                error
            )
        }

    inline fun <B> fold(right: (A) -> B, left: (Throwable) -> B): B =
        when (this) {
            is Right -> right(value)
            is Left -> left(error)
        }

    inline fun orDefault(default: () -> Either<A>): Either<A> =
        when (this) {
            is Right -> this
            is Left -> default()
        }

    inline fun doOnRight(block: (A) -> Unit): Either<A> {
        if (this is Right) block(value)
        return this
    }

    fun ignoreElement(): Either<Unit> = map { Unit }
}

data class Left<A : Any>(val error: Throwable) : Either<A>()
data class Right<A : Any>(val value: A) : Either<A>()

