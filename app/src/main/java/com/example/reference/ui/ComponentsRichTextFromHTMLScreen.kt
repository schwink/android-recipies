package com.example.reference.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.reference.ui.theme.Typography
import com.example.ui.TextH2

private const val HTML = """
    <h1>You can use headings</h1>

    <p><b>Various</b> <i>styles</i> <u>of</u> <s>text</s>, <big>big</big> and <small>small</small></p>

    <p>
        <span style="color: red">c</span>
        <span style="color: green">o</span>
        <span style="color: blue">l</span>
        <span style="background-color: red">o</span>
        <span style="background-color: green">r</span>
        <span style="background-color: blue">s</span>
    </p>

    <p><a href="https://example.com">Links too</a></p>
"""

@Composable
fun ComponentsRichTextFromHTMLScreen() {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = HTML,
                fontFamily = Monospace,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )

            TextH2(text = "Using AnnotatedString.fromHtml")
            Text(
                text = AnnotatedString.fromHtml(
                    htmlString = HTML,
                    linkStyles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                        )
                    ),
                )
            )

            TextH2(text = "With style = Typography.titleLarge")
            Text(
                text = AnnotatedString.fromHtml(
                    htmlString = HTML,
                    linkStyles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                        )
                    ),
                ),
                style = Typography.titleLarge,
            )

            TextH2(text = "With textAlign = Center")
            Text(
                text = AnnotatedString.fromHtml(
                    htmlString = HTML,
                    linkStyles = TextLinkStyles(
                        style = SpanStyle(
                            textDecoration = TextDecoration.Underline,
                        )
                    ),
                ),
                textAlign = Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}