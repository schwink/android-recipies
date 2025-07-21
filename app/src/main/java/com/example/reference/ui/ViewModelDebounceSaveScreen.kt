package com.example.reference.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reference.ui.theme.Typography
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.random.Random

sealed class SavableFieldState<T>(val value: T, val isSaving: Boolean) {
    class Saved<T>(value: T) : SavableFieldState<T>(value, false)
    class InProgress<T>(value: T) : SavableFieldState<T>(value, true)
    class Error<T>(value: T, val error: String) : SavableFieldState<T>(value, true)
}

class DebounceSaveViewModel : ViewModel() {

    private val networkClient = ExampleUnreliableNetworkClient

    private val _valueDebouncer = MutableSharedFlow<String>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val _valueState =
        MutableStateFlow<SavableFieldState<String>>(SavableFieldState.Saved(""))
    val valueState = _valueState.asStateFlow()

    init {
        // Debounced calls to update the server while showing a spinner
        viewModelScope.launch {
            _valueDebouncer
                .debounce(500) // wait ms after last input
                .collect { value ->
                    Log.w("DebounceSave", "Sending $value to network")
                    _valueState.emit(SavableFieldState.InProgress(value))

                    val result = networkClient.commitState(value)
                    result.onSuccess {
                        Log.w("DebounceSave", "Success $value")
                        _valueState.emit(SavableFieldState.Saved(value))
                    }.onFailure { e ->
                        Log.w("DebounceSave", "Failure $value")
                        _valueState.emit(
                            SavableFieldState.Error(
                                value,
                                e.message ?: e.toString()
                            )
                        )
                    }
                }
        }
    }

    fun commit(value: String) {
        // Put the value in the SharedFlow, replacing the last item in the buffer and triggering the
        // debounce timer.
        // success should always be true, because of BufferOverflow.DROP_OLDEST
        val success = _valueDebouncer.tryEmit(value)
        Log.w("DebounceSave", "Submitting $value: $success")
    }
}

@Composable
fun ViewModelDebounceSaveScreen(
    viewModel: DebounceSaveViewModel = viewModel(),
) {
    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = """
                    After 500ms with no further input, the result is committed to the network service.

                    To avoid races at the network layer, once a request is submitted, we wait for it to complete before taking the next one, even if the value has been edited in the meantime.
                """.trimIndent()
            )

            val saveState by viewModel.valueState.collectAsState()

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                var text: String by remember { mutableStateOf("") }
                val isDirty = text != saveState.value

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.commit(it)
                    },
                    label = {
                        Text("Debounced commit")
                    },
                )
                when (saveState) {
                    is SavableFieldState.InProgress -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically),
                        )
                    }

                    is SavableFieldState.Error -> {
                        Button(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = { viewModel.commit(text) },
                        ) {
                            Text("Retry")
                        }
                    }

                    else -> if (isDirty) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically),
                        ) {
                            Text(
                                text = "!",
                                style = Typography.headlineMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            if (saveState is SavableFieldState.Error) {
                Text(
                    text = (saveState as SavableFieldState.Error<String>).error,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

private object ExampleUnreliableNetworkClient {

    // Seeded Random, so behavior is haphazard but repeatable
    private val random = Random(1)

    suspend fun commitState(value: String): Result<String> {
        delay(random.nextLong(500, 1500))

        if (random.nextInt(0, 3) == 0) {
            return Result.failure(IOException("Transport error"))
        }

        return Result.success(value)
    }
}