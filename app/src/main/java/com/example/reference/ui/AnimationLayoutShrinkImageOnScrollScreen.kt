package com.example.reference.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

private val HeaderMaxHeight = 200.dp
private val HeaderMinHeight = 100.dp

@Composable
fun AnimationLayoutShrinkImageOnScrollScreen() {
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
            .background(Color.Red)
            .fillMaxWidth()
            .height(HeaderMaxHeight)
    ) {
        // Calculate dp->px on composition rather than more frequently on layout
        val maxHeightPx = with(LocalDensity.current) { HeaderMaxHeight.roundToPx() }
        val minHeightPx = with(LocalDensity.current) { HeaderMinHeight.roundToPx() }

        Layout(
            content = {
                // Use an icon with rounded corners for this demo, but it could be anything that
                // looks ok when scaled.
                Box(
                    modifier = Modifier
                        .size(HeaderMaxHeight - 32.dp)
                        .padding(16.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            },
        ) { measurables, constraints ->
            check(measurables.size == 1)
            val imageMeasurable = measurables[0]

            val occludedPx = scrollStateProvider().coerceAtMost(maxHeightPx - minHeightPx)
            val collapseFraction: Float =
                1 - ((maxHeightPx - occludedPx).toFloat() / maxHeightPx)

            val imageHeight = lerp(maxHeightPx, 0, collapseFraction)
            val imagePlaceable =
                imageMeasurable.measure(Constraints.fixed(imageHeight, imageHeight))

            layout(
                width = constraints.maxWidth,
                height = imageHeight,
            ) {
                imagePlaceable.placeRelative(0, 0)
            }
        }
    }
}

@Composable
private fun Body(scrollState: ScrollState) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        Spacer(
            modifier = Modifier
                .height(HeaderMaxHeight - HeaderMinHeight)
                .padding(start = 8.dp)
                .width(8.dp)
                .alpha(.2f)
                .background(Color.Blue)
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
                    Scroll me to vary the size of the header image without causing recomposition.

                    This scrollable view sits in front of the header, with spacers preventing it overlapping.

                    The actual size of the header does not change, but its contents are shrunk as this scrolling element occludes it.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}