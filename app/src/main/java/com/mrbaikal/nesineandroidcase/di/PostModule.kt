package com.mrbaikal.nesineandroidcase.di

import com.mrbaikal.nesineandroidcase.data.repository.PostRepositoryImpl
import com.mrbaikal.nesineandroidcase.data.service.PostApi
import com.mrbaikal.nesineandroidcase.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
interface PostModule {

    @Binds
    fun bindPostRepository(repositoryImpl: PostRepositoryImpl): PostRepository

    companion object {
        @Provides
        fun providePostApi(retrofit: Retrofit): PostApi {
            return retrofit.create(PostApi::class.java)
        }
    }
}