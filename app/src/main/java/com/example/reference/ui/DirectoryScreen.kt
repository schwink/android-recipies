package com.example.reference.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.reference.ui.theme.ReferenceTheme

@Composable
fun DirectoryScreen(
    modifier: Modifier = Modifier,
    onSelectCustomNavType: (ExampleCustomNavData) -> Unit,
) {
    Column(modifier = modifier) {
        Text(
            text = "Navigation",
            modifier = Modifier.clickable {
                onSelectCustomNavType(ExampleCustomNavData(value = "Sent this value!"))
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DirectoryScreenPreview() {
    ReferenceTheme {
        DirectoryScreen(
            onSelectCustomNavType = {}
        )
    }
}