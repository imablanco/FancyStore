package com.ablanco.fancystore.domain.base

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 *
 * Represents a known domain error
 */
sealed class DomainError : Throwable()

object GenericError : DomainError()
object ServiceError : DomainError()
