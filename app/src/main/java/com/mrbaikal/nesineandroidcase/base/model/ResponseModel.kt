package com.mrbaikal.nesineandroidcase.base.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel<T>(
    val data: T,
    val error: ErrorModel? = null
)

@Serializable
data class ErrorModel(
    val message: String
)

@Suppress("UNCHECKED_CAST")
suspend fun <T, R> ResponseModel<T>.responseMap(transform: suspend (T) -> R): ResponseModel<R> {
    return when {
        error != null -> ResponseModel(
            data = null as R,
            error = error
        )
        else -> ResponseModel(
            data = transform(data)
        )
    }
}

inline fun <T> ResponseModel<T>.onError(action: (ErrorModel) -> Unit): ResponseModel<T> {
    error?.let(action)
    return this
}