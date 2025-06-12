package com.example.reference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExampleBasicUiState(
    val clickCounter: Int
)

class ExampleBasicViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        ExampleBasicUiState(clickCounter = 0)
    )
    val uiState: StateFlow<ExampleBasicUiState> = _uiState.asStateFlow()

    fun incrementCounter() {
        // viewModelScope runs on the UI thread
        viewModelScope.launch {
            delay(300L)

            _uiState.update {
                // Use the data class copy constructor to create an updated copy
                it.copy(clickCounter = it.clickCounter + 1)
            }
        }
    }
}

@Composable
fun ExampleViewModelBasicScreen(
    viewModel: ExampleBasicViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text("Click counter ${uiState.clickCounter}")
            Button(onClick = { viewModel.incrementCounter() }) {
                Text("Click")
            }
        }
    }
}