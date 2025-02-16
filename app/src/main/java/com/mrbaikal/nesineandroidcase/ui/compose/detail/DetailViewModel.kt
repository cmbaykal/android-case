package com.mrbaikal.nesineandroidcase.ui.compose.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _editMode = MutableStateFlow(false)
    val editMode = _editMode.asStateFlow()

    fun setEditMode(enabled: Boolean) {
        _editMode.update { enabled }
    }
}