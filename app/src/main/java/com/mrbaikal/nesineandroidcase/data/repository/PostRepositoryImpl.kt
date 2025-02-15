package com.mrbaikal.nesineandroidcase.data.repository

import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import com.mrbaikal.nesineandroidcase.base.model.responseMap
import com.mrbaikal.nesineandroidcase.data.model.response.toDomain
import com.mrbaikal.nesineandroidcase.data.service.PostApi
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import com.mrbaikal.nesineandroidcase.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val service: PostApi
) : PostRepository {

    override suspend fun getPosts(): ResponseModel<List<PostModel>> {
        return service.getPosts().responseMap { list -> list.mapIndexed { index, item -> item.toDomain(index) } }
    }
}