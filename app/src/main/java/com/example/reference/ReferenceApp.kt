package com.example.reference

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.navigation.SerializableJsonNavType
import com.example.reference.ui.AnimationFadeInRowOnBottomSheetScreen
import com.example.reference.ui.AnimationLayoutRearrangeOnScrollScreen
import com.example.reference.ui.AnimationOffsetPaddingOnScrollScreen
import com.example.reference.ui.ChromeFullScreenTextInputScreen
import com.example.reference.ui.ComponentsRichTextFromHTMLScreen
import com.example.reference.ui.CustomNavTypeScreen
import com.example.reference.ui.DirectoryScreen
import com.example.reference.ui.ExampleComponentsRoundedCornersScreen
import com.example.reference.ui.ExampleCustomNavData
import com.example.reference.ui.ViewModelDebounceSaveScreen
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

sealed class Destinations {
    @Serializable
    object Directory

    @Serializable
    data class CustomNavType(
        val serializedValue: ExampleCustomNavData?,
    )

    @Serializable
    object ViewModelDebounceSave

    @Serializable
    object ChromeFullScreenTextInput

    @Serializable
    object AnimationOffsetPaddingOnScroll

    @Serializable
    object AnimationLayoutScaleOnScroll

    @Serializable
    object AnimationFadeInRowOnBottomSheet

    @Serializable
    object ComponentsRoundedCorners

    @Serializable
    object ComponentsRichTextFromHTML
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
                    val destination = Destinations.CustomNavType(data)
                    navController.navigate(destination)
                },
                onSelectViewModelDebounceSave = {
                    navController.navigate(Destinations.ViewModelDebounceSave)
                },
                onSelectChromeFullScreenTextInput = {
                    navController.navigate(Destinations.ChromeFullScreenTextInput)
                },
                onSelectAnimationOffsetPaddingOnScroll = {
                    navController.navigate(Destinations.AnimationOffsetPaddingOnScroll)
                },
                onSelectAnimationLayoutRearrangeOnScroll = {
                    navController.navigate(Destinations.AnimationLayoutScaleOnScroll)
                },
                onSelectAnimationFadeInRowOnBottomSheet = {
                    navController.navigate(Destinations.AnimationFadeInRowOnBottomSheet)
                },
                onSelectComponentsRoundedCorners = {
                    navController.navigate(Destinations.ComponentsRoundedCorners)
                },
                onSelectComponentsRichTextFromHTML = {
                    navController.navigate(Destinations.ComponentsRichTextFromHTML)
                }
            )
        }
        composable<Destinations.CustomNavType>(typeMap = typeMap) { backStackEntry ->
            val destination = backStackEntry.toRoute<Destinations.CustomNavType>()

            CustomNavTypeScreen(data = destination.serializedValue)
        }
        composable<Destinations.ViewModelDebounceSave> { backStackEntry ->
            ViewModelDebounceSaveScreen()
        }
        composable<Destinations.ChromeFullScreenTextInput> {
            ChromeFullScreenTextInputScreen()
        }
        composable<Destinations.AnimationOffsetPaddingOnScroll> {
            AnimationOffsetPaddingOnScrollScreen()
        }
        composable<Destinations.AnimationLayoutScaleOnScroll> {
            AnimationLayoutRearrangeOnScrollScreen()
        }
        composable<Destinations.AnimationFadeInRowOnBottomSheet> {
            AnimationFadeInRowOnBottomSheetScreen()
        }
        composable<Destinations.ComponentsRoundedCorners> {
            ExampleComponentsRoundedCornersScreen()
        }
        composable<Destinations.ComponentsRichTextFromHTML> {
            ComponentsRichTextFromHTMLScreen()
        }
    }
}