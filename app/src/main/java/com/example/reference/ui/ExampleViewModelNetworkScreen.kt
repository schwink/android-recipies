package com.example.reference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

sealed class ExampleNetworkUiState {
    data object Loading : ExampleNetworkUiState()
    data class Failure(val message: String) : ExampleNetworkUiState()
    data class Success(val state: ExampleBasicUiState) : ExampleNetworkUiState()
}

sealed class UiEvent {
    data class TransientError(val message: String) : UiEvent()
}

enum class SavingState {
    DONE,
    IN_PROGRESS,
    ERROR,
}

class ExampleNetworkViewModel(private val networkClient: ExampleUnreliableNetworkClient) :
    ViewModel() {

    private val _uiState = MutableStateFlow<ExampleNetworkUiState>(
        ExampleNetworkUiState.Loading
    )
    val uiState: StateFlow<ExampleNetworkUiState> = _uiState.asStateFlow()

    private val _saveableState = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val _isSaving = MutableStateFlow(SavingState.DONE)
    val isSaving: StateFlow<SavingState> = _isSaving.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent> = _uiEvents

    init {
        fetchInitialData()

        // Debounced calls to update the server while showing a spinner
        viewModelScope.launch {
            _saveableState
                .debounce(500) // wait ms after last input
                .flatMapLatest { value ->
                    flow {
                        _isSaving.emit(SavingState.IN_PROGRESS)

                        val result = networkClient.commitState(value)
                        result.onSuccess {
                            emit(Unit)
                            _isSaving.emit(SavingState.DONE)
                        }.onFailure {
                            _uiEvents.emit(
                                UiEvent.TransientError("Failed to commit. Please try again.")
                            )
                            _isSaving.emit(SavingState.ERROR)
                        }
                    }
                }
                .collect {}
        }

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
                _uiEvents.emit(UiEvent.TransientError("Failed to increment, please try again"))
            }
        }
    }

    fun commitSaveable(value: String) {
        // Put the value in the SharedFlow, replacing the last item in the buffer and triggering the
        // debounce timer.
        // Could set loading to true here if want to show the spinner while waiting, instead of only
        // when the network call is in progress.
        _saveableState.tryEmit(value)
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
fun ExampleViewModelNetworkScreen(
    viewModel: ExampleNetworkViewModel,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.TransientError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            when (val currentUiState = uiState) {
                is ExampleNetworkUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ExampleNetworkUiState.Success -> {
                    val state = currentUiState.state
                    Text("Click counter ${state.clickCounter}")
                    Button(onClick = { viewModel.incrementCounter() }) {
                        Text("Fire and forget")
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Row {
                        var saveableState: String by remember { mutableStateOf("") }
                        val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
                        OutlinedTextField(
                            value = saveableState,
                            onValueChange = {
                                saveableState = it
                                viewModel.commitSaveable(it)
                            },
                            label = {
                                Text("Debounced commit")
                            }
                        )
                        when (isSaving) {
                            SavingState.IN_PROGRESS -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }

                            SavingState.ERROR -> {
                                Button(
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    onClick = { viewModel.commitSaveable(saveableState) },
                                ) {
                                    Text("Retry")
                                }
                            }

                            SavingState.DONE -> Unit
                        }
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

        if (Random.nextInt(0, 3) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        counter += random.nextInt(0, 5)

        return Result.success(counter)
    }

    suspend fun incrementCounter(): Result<Int> {
        delay(random.nextLong(500, 1500))

        if (random.nextInt(0, 3) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        counter += 1

        return Result.success(counter)
    }

    suspend fun commitState(value: String): Result<String> {
        delay(random.nextLong(500, 1500))

        if (random.nextInt(0, 3) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        return Result.success(value)
    }
}