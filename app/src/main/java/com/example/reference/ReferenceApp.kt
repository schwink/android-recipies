package com.example.reference

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.navigation.SerializableJsonNavType
import com.example.reference.ui.ChromeFadeInRowOnBottomSheetScreen
import com.example.reference.ui.ChromeFullScreenTextInputScreen
import com.example.reference.ui.ChromeRearrangeLayoutOnScrollScreen
import com.example.reference.ui.ChromeOffsetPaddingOnScrollScreen
import com.example.reference.ui.ComponentsRichTextFromHTMLScreen
import com.example.reference.ui.ComponentsRoundedCornersScreen
import com.example.reference.ui.CustomNavTypeScreen
import com.example.reference.ui.DirectoryScreen
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
    object ChromeOffsetPaddingOnScroll

    @Serializable
    object ChromeRearrangeLayoutOnScroll

    @Serializable
    object ChromeFadeInRowOnBottomSheet

    @Serializable
    object ChromeFullScreenTextInput

    @Serializable
    object ComponentsRichTextFromHTML

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
                    val destination = Destinations.CustomNavType(data)
                    navController.navigate(destination)
                },
                onSelectViewModelDebounceSave = {
                    navController.navigate(Destinations.ViewModelDebounceSave)
                },
                onSelectChromeOffsetPaddingOnScroll = {
                    navController.navigate(Destinations.ChromeOffsetPaddingOnScroll)
                },
                onSelectChromeRearrangeLayoutOnScroll = {
                    navController.navigate(Destinations.ChromeRearrangeLayoutOnScroll)
                },
                onSelectChromeFadeInRowOnBottomSheet = {
                    navController.navigate(Destinations.ChromeFadeInRowOnBottomSheet)
                },
                onSelectChromeFullScreenTextInput = {
                    navController.navigate(Destinations.ChromeFullScreenTextInput)
                },
                onSelectComponentsRichTextFromHTML = {
                    navController.navigate(Destinations.ComponentsRichTextFromHTML)
                },
                onSelectComponentsRoundedCorners = {
                    navController.navigate(Destinations.ComponentsRoundedCorners)
                },
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
        composable<Destinations.ChromeOffsetPaddingOnScroll> {
            ChromeOffsetPaddingOnScrollScreen()
        }
        composable<Destinations.ChromeRearrangeLayoutOnScroll> {
            ChromeRearrangeLayoutOnScrollScreen()
        }
        composable<Destinations.ChromeFadeInRowOnBottomSheet> {
            ChromeFadeInRowOnBottomSheetScreen()
        }
        composable<Destinations.ComponentsRichTextFromHTML> {
            ComponentsRichTextFromHTMLScreen()
        }
        composable<Destinations.ComponentsRoundedCorners> {
            ComponentsRoundedCornersScreen()
        }
    }
}