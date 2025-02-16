package com.mrbaikal.nesineandroidcase.ui.compose.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrbaikal.nesineandroidcase.base.model.onSuccess
import com.mrbaikal.nesineandroidcase.domain.model.PostModel
import com.mrbaikal.nesineandroidcase.domain.repository.PostRepository
import com.mrbaikal.nesineandroidcase.ext.execute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _postList = MutableStateFlow<List<PostModel>>(mutableListOf())
    val postList = _postList.onStart {
        getPosts()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), mutableListOf())

    fun getPosts() {
        viewModelScope.launch {
            execute { repository.getPosts() }.onSuccess { posts ->
                _postList.update { posts }
            }
        }
    }

    fun updateItem(postModel: PostModel?) {
        if (postModel == null) return
        val temp = _postList.value.toMutableList()
        val item = temp.find { it.id == postModel.id }
        val index = temp.indexOf(item)
        temp[index] = postModel
        _postList.update { temp }
    }

    fun deleteItem(index: Int) {
        val temp = _postList.value.toMutableList()
        temp.removeAt(index)
        _postList.update { temp }
    }
}