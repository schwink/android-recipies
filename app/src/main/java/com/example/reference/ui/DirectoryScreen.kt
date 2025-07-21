package com.example.reference.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.TextH1

@Composable
fun DirectoryScreen(
    onSelectCustomNavType: (ExampleCustomNavData) -> Unit,
    onSelectViewModelDebounceSave: () -> Unit,
    onSelectChromeModalTextInput: () -> Unit,
    onSelectAnimationOffsetPaddingOnScroll: () -> Unit,
    onSelectAnimationLayoutRearrangeOnScroll: () -> Unit,
    onSelectAnimationFadeInRowOnBottomSheet: () -> Unit,
    onSelectComponentsRoundedCorners: () -> Unit,
    onSelectComponentsRichTextFromHTML: () -> Unit,
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            TextH1(text = "Navigation")
            Button(onClick = {
                onSelectCustomNavType(ExampleCustomNavData(value = "Sent this value!"))
            }) {
                Text("Pass custom data type to navigation destination")
            }

            TextH1(text = "ViewModel")
            Button(onClick = { onSelectViewModelDebounceSave() }) {
                Text("Debounce save for an input field")
            }

            TextH1(text = "Chrome")
            Button(onClick = { onSelectChromeModalTextInput() }) {
                Text("Modal text input screen reacting to soft keyboard and nav bar")
            }

            TextH1(text = "Animation")
            Button(onClick = { onSelectAnimationOffsetPaddingOnScroll() }) {
                Text("Change header padding on scroll without triggering recomposition")
            }
            Button(onClick = { onSelectAnimationLayoutRearrangeOnScroll() }) {
                Text("Rearrange and resize header components on scroll without triggering recomposition")
            }
            Button(onClick = { onSelectAnimationFadeInRowOnBottomSheet() }) {
                Text("Fade in a row when expanding a bottom sheet")
            }

            TextH1(text = "UI Components")
            Button(onClick = { onSelectComponentsRoundedCorners() }) {
                Text("Rounded corners on various components")
            }
            Button(onClick = { onSelectComponentsRichTextFromHTML() }) {
                Text("Rich text from HTML")
            }
        }
    }
}
