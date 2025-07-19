package com.example.reference.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

private val HeaderMaxHeight = 200.dp
private val HeaderMinHeight = 50.dp

@Composable
fun AnimationOffsetPaddingOnScrollScreen() {
    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            val scrollState = rememberScrollState()

            Header(
                scrollStateProvider = { scrollState.value },
            )

            Column {
                // Spacer to prevent the body overlapping the header when fully scrolled
                Spacer(
                    modifier = Modifier
                        .height(HeaderMinHeight)
                        .width(8.dp)
                        .alpha(.2f)
                        .background(Color.Blue)
                )
                Body(scrollState = scrollState)
            }
        }
    }
}

@Composable
private fun Header(
    scrollStateProvider: () -> Int,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeaderMaxHeight)
            .background(Color.Red)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // Calculate dp->px on composition rather than more frequently on layout
        val maxHeightPx = with(LocalDensity.current) { HeaderMaxHeight.roundToPx() }
        val minHeightPx = with(LocalDensity.current) { HeaderMinHeight.roundToPx() }

        var textHeightPx by remember { mutableIntStateOf(0) }

        Text(
            text = "Shrinking header padding on scroll",
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    // We expect this to only be set once after initial measurement
                    textHeightPx = it.height
                }
                .offset {
                    // Runs on layout only, not composition.
                    val heightPx = (maxHeightPx - scrollStateProvider()).coerceAtLeast(
                        minHeightPx
                    )
                    val paddingTopPx = (heightPx - textHeightPx) / 2
                    IntOffset(x = 0, y = paddingTopPx)
                }
                .background(Color.White)

        )

    }
}

@Composable
private fun Body(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        // Spacer to prevent the content overlapping the header initially
        Spacer(
            modifier = Modifier
                .height(HeaderMaxHeight - HeaderMinHeight)
                .padding(start = 8.dp)
                .width(8.dp)
                .alpha(.2f)
                .background(color = Color.Blue)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(.9f)
                .background(color = Color.White)
                .padding(16.dp)
                .height(1000.dp)
        ) {
            Text(
                text = """
                    Scroll me to vary the center of the header without causing recomposition.

                    This scrollable view sits in front of the header, with spacers preventing it overlapping.

                    The actual size of the header does not change, but its contents are offset to stay centered as this scrolling element occludes it.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}