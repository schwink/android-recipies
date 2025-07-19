package com.example.reference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.reference.ui.theme.ReferenceTheme
import com.example.ui.TextH1

@Composable
fun DirectoryScreen(
    onSelectCustomNavType: (ExampleCustomNavData) -> Unit,
    onSelectViewModelBasic: () -> Unit,
    onSelectViewModelNetwork: () -> Unit,
    onSelectChromeReadme: () -> Unit,
    onSelectChromeModalTextInput: () -> Unit,
    onSelectAnimationOffsetPaddingOnScroll: () -> Unit,
    onSelectComponentsRoundedCorners: () -> Unit,
) {
    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            TextH1(text = "Navigation")
            Button(onClick = {
                onSelectCustomNavType(ExampleCustomNavData(value = "Sent this value!"))
            }) {
                Text("Pass custom data type to navigation destination")
            }

            TextH1(text = "ViewModel")
            Button(onClick = { onSelectViewModelBasic() }) {
                Text("Basic ViewModel setup")
            }
            Button(onClick = { onSelectViewModelNetwork() }) {
                Text("ViewModel with network")
            }

            TextH1(text = "Chrome")
            Button(onClick = { onSelectChromeReadme() }) {
                Text("Gotchas for system bars, emulator behavior, etc.")
            }
            Button(onClick = { onSelectChromeModalTextInput() }) {
                Text("Modal text input screen reacting to soft keyboard and nav bar")
            }

            TextH1(text = "Animation")
            Button(onClick = { onSelectAnimationOffsetPaddingOnScroll() }) {
                Text("Change header padding on scroll without triggering recomposition")
            }

            TextH1(text = "UI Components")
            Button(onClick = { onSelectComponentsRoundedCorners() }) {
                Text("Rounded corners on various components")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DirectoryScreenPreview() {
    ReferenceTheme {
        DirectoryScreen(
            onSelectCustomNavType = {},
            onSelectViewModelBasic = {},
            onSelectViewModelNetwork = {},
            onSelectChromeReadme = {},
            onSelectChromeModalTextInput = {},
            onSelectAnimationOffsetPaddingOnScroll = {},
            onSelectComponentsRoundedCorners = {},
        )
    }
}