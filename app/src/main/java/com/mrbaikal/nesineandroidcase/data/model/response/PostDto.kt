package com.mrbaikal.nesineandroidcase.data.model.response

import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import kotlinx.serialization.Serializable

@Serializable
class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

fun PostDto.toDomain() = PostModel(
    id = id,
    title = title,
    body = body
)