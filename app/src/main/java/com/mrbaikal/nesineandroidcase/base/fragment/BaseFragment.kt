package com.mrbaikal.nesineandroidcase.base.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mrbaikal.nesineandroidcase.ext.message
import com.mrbaikal.nesineandroidcase.ext.progress
import com.mrbaikal.nesineandroidcase.ui.dialog.LoadingDialog
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : ViewModel> : Fragment() {

    abstract val viewModel: VM
    private var loadingDialog: LoadingDialog? = null
    private var messageDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.progress.collect { isLoading ->
                handleProgress(isLoading)
            }
        }

        lifecycleScope.launch {
            viewModel.message.collect { message ->
                message?.let {
                    showMessage(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.dismiss()
        loadingDialog = null
        messageDialog?.dismiss()
        messageDialog = null
    }

    open fun handleProgress(isLoading: Boolean) {
        if (isLoading) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(requireContext())
            }
            loadingDialog?.show()
        } else {
            loadingDialog?.dismiss()
        }
    }

    open fun showMessage(message: String) {
        messageDialog?.dismiss()
        messageDialog = MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("Tamam") { dialog, _ -> dialog.dismiss() }
            .create()
        messageDialog?.show()
    }
} 