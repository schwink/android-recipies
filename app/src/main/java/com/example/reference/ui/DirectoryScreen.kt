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
    onSelectChromeFullScreenTextInput: () -> Unit,
    onSelectChromeOffsetPaddingOnScroll: () -> Unit,
    onSelectChromeRearrangeLayoutOnScroll: () -> Unit,
    onSelectChromeFadeInRowOnBottomSheet: () -> Unit,
    onSelectComponentsRichTextFromHTML: () -> Unit,
    onSelectComponentsRoundedCorners: () -> Unit,
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
            Button(onClick = { onSelectChromeOffsetPaddingOnScroll() }) {
                Text("Change padding of a header item on scroll")
            }
            Button(onClick = { onSelectChromeRearrangeLayoutOnScroll() }) {
                Text("Rearrange and resize header components on scroll")
            }
            Button(onClick = { onSelectChromeFadeInRowOnBottomSheet() }) {
                Text("Fade in a row when expanding a bottom sheet")
            }
            Button(onClick = { onSelectChromeFullScreenTextInput() }) {
                Text("Full-screen text input reacting to the soft keyboard")
            }

            TextH1(text = "UI Components")
            Button(onClick = { onSelectComponentsRichTextFromHTML() }) {
                Text("Rich text from HTML")
            }
            Button(onClick = { onSelectComponentsRoundedCorners() }) {
                Text("Rounded corners on various components")
            }
        }
    }
}
