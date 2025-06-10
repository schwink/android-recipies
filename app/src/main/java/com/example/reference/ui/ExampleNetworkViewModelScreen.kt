package com.example.reference.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

sealed class ExampleNetworkUiState {
    data object Loading : ExampleNetworkUiState()
    data class Failure(val message: String) : ExampleNetworkUiState()
    data class Success(val state: ExampleBasicUiState) : ExampleNetworkUiState()
}

class ExampleNetworkViewModel(private val networkClient: ExampleUnreliableNetworkClient) :
    ViewModel() {

    private val _uiState = MutableStateFlow<ExampleNetworkUiState>(
        ExampleNetworkUiState.Loading
    )
    val uiState: StateFlow<ExampleNetworkUiState> = _uiState.asStateFlow()

    init {
        fetchInitialData()
    }

    fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update {
                ExampleNetworkUiState.Loading
            }

            val result = networkClient.fetchCounter()

            result.onSuccess {
                val counter = result.getOrThrow()
                _uiState.update {
                    ExampleNetworkUiState.Success(ExampleBasicUiState(counter))
                }
            }.onFailure {
                val error = result.exceptionOrNull()
                _uiState.update {
                    ExampleNetworkUiState.Failure(
                        error?.message ?: "Failed to load"
                    )
                }
            }
        }
    }

    fun incrementCounter() {
        viewModelScope.launch {
            val result = networkClient.incrementCounter()
            result.onSuccess {
                val counter = result.getOrThrow()
                _uiState.update { oldState ->
                    when (oldState) {
                        is ExampleNetworkUiState.Success -> {
                            oldState.copy(
                                state = oldState.state.copy(clickCounter = counter)
                            )
                        }

                        else -> {
                            ExampleNetworkUiState.Success(
                                ExampleBasicUiState(clickCounter = counter)
                            )
                        }
                    }
                }
            }.onFailure { e ->
                Log.e("ExampleNetworkViewModel", "Failed to load", e)
            }
        }
    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            // Use a Factory to inject the network dependency.
            return ExampleNetworkViewModel(
                networkClient = ExampleUnreliableNetworkClient(counter = 0)
            ) as T
        }

    }
}

@Composable
fun ExampleNetworkViewModelScreen(
    viewModel: ExampleNetworkViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            when (val currentUiState = uiState) {
                is ExampleNetworkUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ExampleNetworkUiState.Success -> {
                    val state = currentUiState.state
                    Text("Click counter ${state.clickCounter}")
                    Button(onClick = { viewModel.incrementCounter() }) {
                        Text("Click")
                    }
                }

                is ExampleNetworkUiState.Failure -> {
                    Text(
                        text = "Error loading",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error)
                    )
                    Button(onClick = { viewModel.fetchInitialData() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

class ExampleUnreliableNetworkClient(private var counter: Int) {

    private val random = Random(1)

    suspend fun fetchCounter(): Result<Int> {
        delay(random.nextLong(500, 1500))

        if (random.nextInt(0, 3) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        counter += random.nextInt(0, 5)

        return Result.success(counter)
    }

    suspend fun incrementCounter(): Result<Int> {
        delay(random.nextLong(500, 1500))

        if (random.nextInt(0, 5) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        counter += 1

        return Result.success(counter)
    }
}