package com.mrbaikal.nesineandroidcase.base.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View?) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.let { imm.hideSoftInputFromWindow(it.windowToken, 0) }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
           capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}