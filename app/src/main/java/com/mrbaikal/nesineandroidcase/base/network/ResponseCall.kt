package com.mrbaikal.nesineandroidcase.base.network

import com.mrbaikal.nesineandroidcase.base.model.ErrorModel
import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ResponseCall<T : Any>(
    private val delegate: Call<T>
) : Call<ResponseModel<T>> {

    override fun enqueue(callback: Callback<ResponseModel<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.onResponse(
                            this@ResponseCall,
                            Response.success(ResponseModel(data = body))
                        )
                    } else {
                        callback.onResponse(
                            this@ResponseCall,
                            Response.success(
                                ResponseModel(
                                    data = null as T,
                                    error = ErrorModel("Sunucudan boş yanıt alındı")
                                )
                            )
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    callback.onFailure(
                        this@ResponseCall,
                        IOException(errorBody ?: response.message() ?: "Bilinmeyen bir hata oluştu")
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(this@ResponseCall, t)
            }
        })
    }

    override fun clone(): Call<ResponseModel<T>> = ResponseCall(delegate.clone())

    override fun execute(): Response<ResponseModel<T>> {
        throw UnsupportedOperationException("ResponseCall doesn't support execute")
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
} 