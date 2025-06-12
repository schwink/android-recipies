package com.example.reference.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun ExampleChromeModalTextInputScreen() {

    val activity = LocalActivity.current
    activity?.run {
        // Optionally, the nav bar can be hidden
        DisposableEffect(Unit) {
            val insetController = WindowInsetsControllerCompat(window, window.decorView)
            val previousBehavior = insetController.systemBarsBehavior
            val previouslyVisible =
                ViewCompat.getRootWindowInsets(window.decorView)
                    ?.isVisible(WindowInsetsCompat.Type.navigationBars()) == true

            insetController.apply {
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsetsCompat.Type.navigationBars())
            }

            onDispose {
                insetController.apply {
                    systemBarsBehavior = previousBehavior
                    if (previouslyVisible) {
                        show(WindowInsetsCompat.Type.navigationBars())
                    } else {
                        hide(WindowInsetsCompat.Type.navigationBars())
                    }
                }
            }
        }
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                // Order can be significant between paddings and fillMaxSize
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .imePadding() // Pad this element to make room for the keyboard when it opens
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = """
                "Full-page" screen that fills the whole screen and does not scroll.

                The screen background color extends behind the status bar on top and the nav bar on bottom.

                Tapping on a text input raises the keyboard and shrinks the screen content.

                In the <activity> entry in AndroidManifest.xml, android:windowSoftInputMode="adjustNothing" prevents content jumping around as the keyboard comes up, instead relying on Modifier.imePadding() to handle it.
            """.trimIndent(),
            )

            var text by remember { mutableStateOf("") }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(1f),
                value = text,
                onValueChange = {
                    text = it
                },
                placeholder = {
                    Text(
                        text = "Text input",
                    )
                },
            )
        }
    }
}