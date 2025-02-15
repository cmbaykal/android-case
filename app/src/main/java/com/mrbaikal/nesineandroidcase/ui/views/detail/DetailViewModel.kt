package com.mrbaikal.nesineandroidcase.ui.views.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor() : ViewModel() {

    private val _editMode = MutableLiveData(false)
    val editMode: LiveData<Boolean> = _editMode

    fun setEditMode(enabled: Boolean) {
        _editMode.postValue(enabled)
    }
}