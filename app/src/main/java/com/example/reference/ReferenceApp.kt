package com.example.reference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.navigation.SerializableJsonNavType
import com.example.reference.ui.DirectoryScreen
import com.example.reference.ui.ExampleBasicViewModel
import com.example.reference.ui.ExampleBasicViewModelScreen
import com.example.reference.ui.ExampleCustomNavData
import com.example.reference.ui.ExampleCustomNavTypeScreen
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
    object BasicViewModel
}

object ExampleCustomNavType :
    SerializableJsonNavType<ExampleCustomNavData>(ExampleCustomNavData.serializer())

@Composable
fun ReferenceApp(modifier: Modifier) {
    val navController = rememberNavController()

    val typeMap = mapOf(
        Pair(typeOf<ExampleCustomNavData?>(), ExampleCustomNavType)
    )

    NavHost(
        modifier = modifier,
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
                onSelectBasicViewModel = {
                    val destination = Destinations.BasicViewModel
                    navController.navigate(destination)
                }
            )
        }
        composable<Destinations.ExampleCustomNavType>(typeMap = typeMap) { backStackEntry ->
            val destination = backStackEntry.toRoute<Destinations.ExampleCustomNavType>()

            ExampleCustomNavTypeScreen(data = destination.initialValue)
        }
        composable<Destinations.BasicViewModel> { backStackEntry ->
            // Create a new ViewModel with this destination as its scope.
            // It will be destroyed when navigating back.
            val viewModel = viewModel(
                modelClass = ExampleBasicViewModel::class,
                // Pass a unique key, so we get a fresh model if there are ever multiple concurrent
                // instances
                key = backStackEntry.id,
            )

            ExampleBasicViewModelScreen(viewModel = viewModel)
        }
    }
}