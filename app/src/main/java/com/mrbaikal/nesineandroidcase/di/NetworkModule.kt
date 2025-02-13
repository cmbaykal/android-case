package com.mrbaikal.nesineandroidcase.di

import com.mrbaikal.nesineandroidcase.BuildConfig
import com.mrbaikal.nesineandroidcase.base.network.ResponseCallAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
    }

    @Provides
    fun provideBaseUrl(): BaseUrl {
        return BaseUrl(BuildConfig.BASE_URL)
    }

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient = OkHttpClient.Builder()
        .apply { interceptors.forEach(::addInterceptor) }
        .build()

    @Provides
    fun provideRetrofit(baseUrl: BaseUrl, client: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl.value)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaType()),
            )
            .addCallAdapterFactory(ResponseCallAdapter.Factory())
            .build()
    }
}

class BaseUrl(val value: String)
