package com.mrbaikal.nesineandroidcase.ui.compose

import androidx.lifecycle.ViewModel
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainComposeViewModel @Inject constructor() : ViewModel() {

    private val _selectedPost = MutableStateFlow<PostModel?>(null)
    val selectedPost = _selectedPost.asStateFlow()

    fun setPostModel(postModel: PostModel) {
        _selectedPost.update { postModel }
    }

    fun editPost(title: String, body: String) {
        _selectedPost.update {
            it?.copy(
                title = title,
                body = body
            )
        }
    }
}