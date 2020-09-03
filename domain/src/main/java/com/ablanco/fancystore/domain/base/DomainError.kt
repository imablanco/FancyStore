package com.ablanco.fancystore.domain.base

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
sealed class DomainError

object GenericError : DomainError()
object ServiceError : DomainError()
