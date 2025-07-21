package com.example.reference.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable

@Serializable
data class ExampleCustomNavData(
    val value: String
)

@Composable
fun CustomNavTypeScreen(
    data: ExampleCustomNavData?
) {
    Scaffold { contentPadding ->
        Text(
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp),
            text = data?.value ?: "<null>"
        )
    }
}