package com.mrbaikal.nesineandroidcase.domain.repository


import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import com.mrbaikal.nesineandroidcase.domain.model.PostModel

interface PostRepository {
    suspend fun getPosts(): ResponseModel<List<PostModel>>
}