package com.example.reference.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
data class ExampleCustomNavData(
    val value: String
)

@Composable
fun ExampleCustomNavTypeScreen(
    modifier: Modifier = Modifier,
    data: ExampleCustomNavData?
) {
    Text(modifier = modifier, text = data?.value ?: "<null>")
}