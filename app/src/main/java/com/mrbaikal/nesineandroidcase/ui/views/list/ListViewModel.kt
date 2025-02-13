package com.mrbaikal.nesineandroidcase.ui.views.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import com.mrbaikal.nesineandroidcase.domain.repository.PostRepository
import com.mrbaikal.nesineandroidcase.ext.execute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _postList = MutableLiveData<List<PostModel>>()
    val postList: LiveData<List<PostModel>> = _postList

    init {
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            repository.getPosts().data?.let { posts ->
                _postList.value = posts
            }
        }
    }

    fun deleteItem(index: Int) {
        val temp = _postList.value?.toMutableList() ?: mutableListOf()
        temp.removeAt(index)
        _postList.postValue(temp)
    }
}