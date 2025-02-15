package com.mrbaikal.nesineandroidcase.ext

import androidx.lifecycle.ViewModel
import com.mrbaikal.nesineandroidcase.base.model.ErrorModel
import com.mrbaikal.nesineandroidcase.base.model.ResponseModel
import com.mrbaikal.nesineandroidcase.base.model.onError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

private const val PROGRESS_KEY = "progress"
private const val MESSAGE_KEY = "message"

suspend inline fun <T> ViewModel.execute(
    crossinline call: suspend () -> ResponseModel<T>,
): ResponseModel<T> {
    try {
        showProgress(true)
        return call.invoke()
    } catch (e: Exception) {
        val message = e.message ?: "Bir hata oluştu"
        showMessage(message)
        return ResponseModel(
            data = null as T,
            error = ErrorModel(message = message)
        )
    } finally {
        showProgress(false)
    }
}

val ViewModel.progress: Flow<Boolean>
    get() = getProgressChannel().receiveAsFlow()

fun ViewModel.showProgress(visible: Boolean) {
    getProgressChannel().trySend(visible)
}

private fun ViewModel.getProgressChannel(): ClosableChannel<Boolean> {
    return getCloseable(PROGRESS_KEY)
        ?: ClosableChannel(Channel<Boolean>(Channel.UNLIMITED)).also {
            addCloseable(PROGRESS_KEY, it)
        }
}

val ViewModel.message: Flow<String?>
    get() = getMessageChannel().receiveAsFlow()

fun ViewModel.showMessage(message: String?) {
    getMessageChannel().trySend(message)
}

private fun ViewModel.getMessageChannel(): ClosableChannel<String?> {
    return getCloseable(MESSAGE_KEY)
        ?: ClosableChannel(Channel<String?>(Channel.UNLIMITED)).also {
            addCloseable(MESSAGE_KEY, it)
        }
}

private class ClosableChannel<T>(private val channel: Channel<T>) : Channel<T> by channel,
    AutoCloseable {
    override fun close() {
        channel.close()
        channel.cancel()
    }
}