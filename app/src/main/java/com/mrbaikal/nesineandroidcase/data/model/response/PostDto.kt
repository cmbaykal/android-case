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

fun PostDto.toDomain(index: Int) = PostModel(
    id = id,
    title = title,
    body = body,
    imgUrl = "https://picsum.photos/300/300?random=$index&grayscale"
)