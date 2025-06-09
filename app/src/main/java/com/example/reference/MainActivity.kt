package com.example.reference

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.reference.ui.theme.ReferenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReferenceTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) { innerPadding ->
                    ReferenceApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}