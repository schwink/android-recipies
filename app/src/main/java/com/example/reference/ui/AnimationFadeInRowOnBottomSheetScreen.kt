package com.example.reference.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationFadeInRowOnBottomSheetScreen() {
    val peekHeight = 400.dp
    val windowHeightPx = LocalWindowInfo.current.containerSize.height
    val peekTopOffsetPx = with(LocalDensity.current) { windowHeightPx - peekHeight.roundToPx() }

    Scaffold { contentPadding ->
        val sheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberStandardBottomSheetState()
        )

        val sheetTopOffsetPx: () -> Int = {
            try {
                sheetScaffoldState.bottomSheetState.requireOffset().toInt()
            } catch (_: IllegalStateException) {
                0
            }
        }

        BottomSheetScaffold(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth(),
            snackbarHost = {},
            sheetContent = {
                SheetBody(
                    peekTopOffsetPx = peekTopOffsetPx,
                    sheetTopOffsetPx = sheetTopOffsetPx,
                )
            },
            scaffoldState = sheetScaffoldState,
            sheetPeekHeight = peekHeight,
        ) {
            Text(
                text = "Here is the background",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetBody(
    modifier: Modifier = Modifier,
    peekTopOffsetPx: Int,
    sheetTopOffsetPx: () -> Int,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Here is the sheet",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        val itemHeight = 96.dp
        val itemHeightPx = with(LocalDensity.current) { itemHeight.roundToPx() }

        // Expand the item starting a little above the peek height, then fade in the alpha
        val startExpandingTopOffsetPx = peekTopOffsetPx - 200
        val startFadingTopOffsetPx = startExpandingTopOffsetPx - itemHeightPx

        Layout(
            content = {
                Text(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer({
                            // Fade in the alpha over 100px
                            val progress =
                                (startFadingTopOffsetPx - sheetTopOffsetPx()).coerceIn(0, 100)
                                    .toFloat() / 100
                            alpha = progress
                        })
                        .padding(4.dp)
                        .background(Color.Red)
                        .padding(16.dp),
                    text = "Here is an element to show only when the sheet is expanded",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        ) { measurables, constraints ->
            check(measurables.size == 1)
            val childMeasurable = measurables[0]

            val childPlaceable = childMeasurable.measure(constraints)

            // Expand height of the containing layout
            val layoutHeight =
                (startExpandingTopOffsetPx - sheetTopOffsetPx()).coerceIn(
                    0,
                    childPlaceable.height
                )

            layout(
                width = constraints.maxWidth,
                height = layoutHeight,
            ) {
                childPlaceable.placeRelative(0, 0)
            }
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = (1..10).toList(),
                key = { i -> i.toString() }
            ) { i ->
                Text(
                    text = "Scrollable sheet content $i of 10",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .height(96.dp)
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .padding(4.dp),
                )
            }
        }
    }
}