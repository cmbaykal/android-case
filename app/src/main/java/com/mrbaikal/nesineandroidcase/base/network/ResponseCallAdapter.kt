package com.mrbaikal.nesineandroidcase.base.network

import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResponseCallAdapter<T : Any>(
    private val responseType: Type
) : CallAdapter<T, Call<ResponseModel<T>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<ResponseModel<T>> {
        return ResponseCall(call)
    }

    class Factory : CallAdapter.Factory() {
        override fun get(
            returnType: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): CallAdapter<*, *>? {
            if (getRawType(returnType) != Call::class.java) return null
            check(returnType is ParameterizedType) { "Return type must be parameterized as Call<ResponseModel<T>>" }

            val responseType = getParameterUpperBound(0, returnType)
            if (getRawType(responseType) != ResponseModel::class.java) return null
            check(responseType is ParameterizedType) { "Response type must be parameterized as ResponseModel<T>" }

            val successType = getParameterUpperBound(0, responseType)
            return ResponseCallAdapter<Any>(successType)
        }
    }
} 