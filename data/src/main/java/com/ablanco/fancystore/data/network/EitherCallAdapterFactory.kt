package com.ablanco.fancystore.data.network

import com.ablanco.fancystore.domain.base.Either
import com.ablanco.fancystore.domain.base.Left
import com.ablanco.fancystore.domain.base.Right
import com.ablanco.fancystore.domain.base.ServiceError
import okhttp3.Request
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Álvaro Blanco Cabrero on 03/09/2020.
 * FancyStore.
 */
/**
 * [CallAdapter.Factory] that returns [EitherCallAdapter] or [EitherCallSuspendAdapter]
 * for calls typed with [Either]
 */
class EitherCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        /*Fail fast if the type is not a ParameterizedType*/
        if (returnType !is ParameterizedType) return null

        /*Check what is our container type. If its of type Call and parameterized with Either, it
         means it comes from a suspend fun, so the adapter needs to handle it*/
        if (getRawType(returnType) == Call::class.java) {
            val enclosingType = getParameterUpperBound(0, returnType)
            if (getRawType(enclosingType) == Either::class.java) {
                return EitherCallSuspendAdapter<Any>(
                    getParameterUpperBound(
                        0,
                        enclosingType as ParameterizedType
                    )
                )
            }
        }

        /*The return type is directly Either, so handle it normally*/
        if (getRawType(returnType) == Either::class.java) {
            return EitherCallAdapter<Any>(
                getParameterUpperBound(
                    0,
                    returnType
                )
            )
        }

        return null
    }
}

/**
 * [CallAdapter] that wraps call responses into [Either].
 */
class EitherCallAdapter<T : Any>(private val returnType: Type) : CallAdapter<T, Either<T>> {

    override fun adapt(call: Call<T>): Either<T> =
        runCatching { EitherCallMapper(call).execute() }.fold(
            onSuccess = { it.body() ?: Left(ServiceError) },
            onFailure = { Left(ServiceError) }
        )

    override fun responseType(): Type = returnType
}

/**
 * [CallAdapter] that wraps suspend calls responses into [Either].
 */
class EitherCallSuspendAdapter<T : Any>(private val returnType: Type) :
    CallAdapter<T, Call<Either<T>>> {

    override fun adapt(call: Call<T>): Call<Either<T>> = EitherCallMapper(call)

    override fun responseType(): Type = returnType
}

/**
 * Simple [Call] that allows mapping from source.
 */
abstract class CallMapper<I, O>(protected val source: Call<I>) : Call<O> {

    final override fun cancel() = source.cancel()
    final override fun request(): Request = source.request()
    final override fun isExecuted() = source.isExecuted
    final override fun isCanceled() = source.isCanceled
}

/**
 * [CallMapper] implementation that transforms source response to [Either].
 * This mapper *NEVER* returns a unsuccessful call nor invokes [Callback.onFailure] as
 * all errors are driven through [Left] instances.
 */
class EitherCallMapper<T : Any>(source: Call<T>) : CallMapper<T, Either<T>>(source) {

    private fun <T : Any> Response<T>.handle(): Either<T> {
        return if (isSuccessful) {
            body()?.let { Right(it) } ?: Left(ServiceError)
        } else {
            Left(ServiceError)
        }
    }

    override fun execute(): Response<Either<T>> =
        runCatching { source.execute() }.fold(
            onSuccess = { Response.success(it.handle()) },
            onFailure = { Response.success(Left(ServiceError)) }
        )

    override fun enqueue(callback: Callback<Either<T>>) {

        source.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@EitherCallMapper,
                    Response.success(response.handle())
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@EitherCallMapper,
                    Response.success(Left(ServiceError))
                )
            }
        })
    }

    override fun clone() = EitherCallMapper(source.clone())
}

