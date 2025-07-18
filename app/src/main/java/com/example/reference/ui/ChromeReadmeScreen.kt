package com.example.reference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.example.ui.TextH1

@Composable
fun ChromeReadmeScreen() {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            TextH1("Software Keyboards on Android Emulators")
            Text(
                modifier = Modifier.padding(8.dp),
                text = """
                If you see a bar on the left edge of the screen when a TextField is selected, it means that Android is trying to use a stylus input.

                In the menu on that bar, go into the guest's Android OS settings and disable stylus input.

                This enables the usual soft keyboard to appear on the bottom of the screen.
            """.trimIndent(),
            )

            TextH1("API 34 vs API 35 system bar background color")
            Text(
                modifier = Modifier.padding(8.dp),
                text = api3435SystemBarText()
            )

            TextH1("Software keyboard background color")
            Text(
                modifier = Modifier.padding(8.dp),
                text = """
                    On some devices, the keyboard will style itself with the app background color, similar to the status bar in edge-to-edge mode.
                    
                    On other devices, the keyboard keeps a default color.
                    
                    Unsure what exactly is responsible for this. Similar versions of Gboard in all cases.
            """.trimIndent(),
            )
        }
    }
}

@Composable
fun api3435SystemBarText(): AnnotatedString {
    val uriHandler = LocalUriHandler.current
    return remember {
        buildAnnotatedString {
            append(
                "To maintain consistent behavior, need to manually specify the system bar background and content colors.\n\n"
            )

            val link =
                LinkAnnotation.Url(
                    "https://www.droidcon.com/2025/02/04/the-elephant-in-the-room-for-android-devs-jetpack-compose-and-edge-to-edge-on-android-15/",
                    TextLinkStyles(
                        SpanStyle(
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                ) {
                    val url = (it as LinkAnnotation.Url).url
                    uriHandler.openUri(url)
                }
            withLink(link) { append("Good write-up here") }
        }
    }
}