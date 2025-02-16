package com.mrbaikal.nesineandroidcase.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mrbaikal.nesineandroidcase.R
import com.mrbaikal.nesineandroidcase.ui.compose.detail.DetailScreen
import com.mrbaikal.nesineandroidcase.ui.compose.list.ListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainComposeActivity : ComponentActivity() {

    private val viewModel: MainComposeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                getColor(R.color.blue),
                getColor(R.color.blue)
            )
        )
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        setContent {
            val navController = rememberNavController()

            NavHost(navController, ScreenRoute.List) {
                composable<ScreenRoute.List> {
                    ListScreen(
                        activityViewModel = viewModel,
                        navigateDetail = { navController.navigate(ScreenRoute.Detail) }
                    )
                }
                composable<ScreenRoute.Detail> {
                    DetailScreen(
                        activityViewModel = viewModel,
                        navigateToBack = navController::popBackStack
                    )
                }
            }
        }
    }
}