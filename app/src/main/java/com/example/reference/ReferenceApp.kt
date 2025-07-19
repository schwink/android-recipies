package com.example.reference

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.navigation.SerializableJsonNavType
import com.example.reference.ui.AnimationOffsetPaddingOnScrollScreen
import com.example.reference.ui.ChromeReadmeScreen
import com.example.reference.ui.DirectoryScreen
import com.example.reference.ui.ExampleBasicViewModel
import com.example.reference.ui.ExampleChromeModalTextInputScreen
import com.example.reference.ui.ExampleComponentsRoundedCornersScreen
import com.example.reference.ui.ExampleCustomNavData
import com.example.reference.ui.ExampleCustomNavTypeScreen
import com.example.reference.ui.ExampleNetworkViewModel
import com.example.reference.ui.ExampleViewModelBasicScreen
import com.example.reference.ui.ExampleViewModelNetworkScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed class Destinations {
    @Serializable
    object Directory

    @Serializable
    data class ExampleCustomNavType(
        val initialValue: ExampleCustomNavData?,
    )

    @Serializable
    object ViewModelBasic

    @Serializable
    object ViewModelNetwork

    @Serializable
    object ChromeReadme

    @Serializable
    object ChromeModalTextInput

    @Serializable
    object AnimationOffsetPaddingOnScroll

    @Serializable
    object ComponentsRoundedCorners
}

object ExampleCustomNavType :
    SerializableJsonNavType<ExampleCustomNavData>(ExampleCustomNavData.serializer())

@Composable
fun ReferenceApp() {
    val navController = rememberNavController()

    val typeMap = mapOf(
        Pair(typeOf<ExampleCustomNavData?>(), ExampleCustomNavType)
    )

    NavHost(
        navController = navController,
        startDestination = Destinations.Directory,
        typeMap = typeMap,
    ) {
        composable<Destinations.Directory> {
            DirectoryScreen(
                onSelectCustomNavType = { data ->
                    val destination = Destinations.ExampleCustomNavType(data)
                    navController.navigate(destination)
                },
                onSelectViewModelBasic = {
                    val destination = Destinations.ViewModelBasic
                    navController.navigate(destination)
                },
                onSelectViewModelNetwork = {
                    val destination = Destinations.ViewModelNetwork
                    navController.navigate(destination)
                },
                onSelectChromeReadme = {
                    navController.navigate(Destinations.ChromeReadme)

                },
                onSelectChromeModalTextInput = {
                    navController.navigate(Destinations.ChromeModalTextInput)
                },
                onSelectAnimationOffsetPaddingOnScroll = {
                    navController.navigate(Destinations.AnimationOffsetPaddingOnScroll)
                },
                onSelectComponentsRoundedCorners = {
                    navController.navigate(Destinations.ComponentsRoundedCorners)
                }
            )
        }
        composable<Destinations.ExampleCustomNavType>(typeMap = typeMap) { backStackEntry ->
            val destination = backStackEntry.toRoute<Destinations.ExampleCustomNavType>()

            ExampleCustomNavTypeScreen(data = destination.initialValue)
        }
        composable<Destinations.ViewModelBasic> { backStackEntry ->
            // Create a new ViewModel with this destination as its scope.
            // It will be destroyed when navigating back.
            val viewModel = viewModel(
                modelClass = ExampleBasicViewModel::class,
                // Pass a unique key, so we get a fresh model if there are ever multiple concurrent
                // instances
                key = backStackEntry.id,
            )

            ExampleViewModelBasicScreen(viewModel = viewModel)
        }
        composable<Destinations.ViewModelNetwork> { backStackEntry ->
            val viewModel = viewModel<ExampleNetworkViewModel>(
                factory = ExampleNetworkViewModel.Factory(),
            )

            ExampleViewModelNetworkScreen(viewModel = viewModel)
        }
        composable<Destinations.ChromeReadme> {
            ChromeReadmeScreen()
        }
        composable<Destinations.ChromeModalTextInput> {
            ExampleChromeModalTextInputScreen()
        }
        composable<Destinations.AnimationOffsetPaddingOnScroll> {
            AnimationOffsetPaddingOnScrollScreen()
        }
        composable<Destinations.ComponentsRoundedCorners> {
            ExampleComponentsRoundedCornersScreen()
        }
    }
}