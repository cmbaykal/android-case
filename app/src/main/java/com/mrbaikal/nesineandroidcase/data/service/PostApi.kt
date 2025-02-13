package com.mrbaikal.nesineandroidcase.data.service

import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import com.mrbaikal.nesineandroidcase.data.model.response.PostDto
import retrofit2.http.GET

interface PostApi {

    @GET("posts")
    suspend fun getPosts(): ResponseModel<List<PostDto>>
}