package com.example.reference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.reference.ui.theme.ReferenceTheme
import com.example.ui.TextH1

@Composable
fun DirectoryScreen(
    modifier: Modifier = Modifier,
    onSelectCustomNavType: (ExampleCustomNavData) -> Unit,
    onSelectBasicViewModel: () -> Unit,
    onSelectNetworkViewModel: () -> Unit,
) {
    Column(modifier = modifier) {
        TextH1(text = "Navigation")
        Button(onClick = {
            onSelectCustomNavType(ExampleCustomNavData(value = "Sent this value!"))
        }) {
            Text("Pass custom data type to navigation destination")
        }

        TextH1(text = "ViewModel")
        Button(onClick = { onSelectBasicViewModel() }) {
            Text("Basic ViewModel setup")
        }
        Button(onClick = { onSelectNetworkViewModel() }) {
            Text("ViewModel with network")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DirectoryScreenPreview() {
    ReferenceTheme {
        DirectoryScreen(
            onSelectCustomNavType = {},
            onSelectBasicViewModel = {},
            onSelectNetworkViewModel = {},
        )
    }
}