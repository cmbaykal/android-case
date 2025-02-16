package com.mrbaikal.nesineandroidcase.ui.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewViewModel @Inject constructor() : ViewModel() {

    private val _selectedPost = MutableLiveData<PostModel?>(null)
    val selectedPost: LiveData<PostModel?> = _selectedPost

    fun setPostModel(postModel: PostModel) {
        _selectedPost.postValue(postModel)
    }

    fun editPost(title: String, body: String) {
        _selectedPost.postValue(_selectedPost.value?.copy(title = title,body = body))
    }
}