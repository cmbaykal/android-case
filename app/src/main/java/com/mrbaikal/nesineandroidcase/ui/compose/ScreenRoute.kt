package com.mrbaikal.nesineandroidcase.ui.compose

import kotlinx.serialization.Serializable

sealed class ScreenRoute {

    @Serializable
    data object List

    @Serializable
    data object Detail

}