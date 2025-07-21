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
import com.example.reference.ui.theme.Typography

private val HeaderMaxHeight = 300.dp
private val HeaderMinHeight = 100.dp

@Composable
fun ChromeRearrangeLayoutOnScrollScreen() {
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
    ) {
        // Calculate dp->px on composition rather than more frequently on layout
        val headerMaxHeightPx = with(LocalDensity.current) { HeaderMaxHeight.roundToPx() }
        val headerMinHeightPx = with(LocalDensity.current) { HeaderMinHeight.roundToPx() }

        Layout(
            content = {
                // Use an icon with rounded corners for this demo, but it could be anything that
                // looks ok when scaled.
                Box(
                    modifier = Modifier
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

                Box(
                    modifier = Modifier.padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Some Text Value",
                        style = Typography.headlineLarge,
                    )
                }
            },
        ) { measurables, constraints ->
            check(measurables.size == 2)
            val imageMeasurable = measurables[0]
            val titleMeasurable = measurables[1]

            val finalHeightChangePx = headerMaxHeightPx - headerMinHeightPx
            // Between none=0.0 and complete=1.0, how much of the transformation has happened?
            val progress: Float =
                (scrollStateProvider().toFloat() / finalHeightChangePx).coerceAtMost(maximumValue = 1f)

            // The image takes up 3/4 of the height
            // Shrink the square image and move from the center to the right of the layout
            val imageInitialSize = headerMaxHeightPx * 3 / 4
            val imageFinalSize = headerMinHeightPx
            val imageSize = lerp(imageInitialSize, imageFinalSize, progress)
            val imageStartOffset =
                lerp(
                    // From centered on the screen
                    (constraints.maxWidth - imageInitialSize) / 2,
                    // To the right edge
                    constraints.maxWidth - imageFinalSize,
                    progress
                )
            val imagePlaceable =
                imageMeasurable.measure(Constraints.fixed(imageSize, imageSize))

            // The title takes up 1/4 of the height
            // Keep the title size but move up and left to be next to the image
            val titlePlaceable = titleMeasurable.measure(
                Constraints.fixedHeight(
                    headerMaxHeightPx / 4
                )
            )
            val titleStartOffset = lerp(
                // From the center
                (constraints.maxWidth - titlePlaceable.width) / 2,
                // To the left edge, centered in the space to the left of the image
                (constraints.maxWidth - imageFinalSize - titlePlaceable.width) / 2,
                progress
            )
            val titleTopOffset = lerp(
                // From just below the image
                headerMaxHeightPx * 3 / 4,
                // To centered
                (headerMinHeightPx - titlePlaceable.height) / 2,
                progress
            )

            layout(
                width = constraints.maxWidth,
                height = lerp(headerMaxHeightPx, headerMinHeightPx, progress),
            ) {
                imagePlaceable.placeRelative(imageStartOffset, 0)

                titlePlaceable.placeRelative(titleStartOffset, titleTopOffset)
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
                    Scroll me to vary the size of the header, changing size and location of the header components without causing recomposition.

                    This scrollable view sits in front of the header, with spacers preventing it overlapping.
                    
                    The header is a custom layout, which allows us to size and place its contents in the layout phase without causing recomposition.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}