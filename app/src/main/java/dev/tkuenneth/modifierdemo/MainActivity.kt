package dev.tkuenneth.modifierdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.magnifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModifierDemo()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModifierDemo() {
    MaterialExpressiveTheme(
        colorScheme = defaultColorScheme()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) }
                )
            }
        ) { innerPadding ->
            var magnifierActive by remember { mutableStateOf(false) }
            var touchPosition by remember { mutableStateOf(Offset.Unspecified) }
            val contentAreaModifier = if (magnifierActive) {
                Modifier
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { touchPosition = it },
                            onDrag = { _, delta -> touchPosition += delta },
                            onDragEnd = { touchPosition = Offset.Unspecified },
                            onDragCancel = { touchPosition = Offset.Unspecified }
                        )
                    }
                    .magnifier(
                        sourceCenter = { touchPosition },
                        magnifierCenter = { touchPosition },
                        zoom = 4F
                    )
            } else {
                Modifier
            }
            Column(
                modifier = contentAreaModifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                MagnifierDemo(magnifierActive = magnifierActive) { magnifierActive = it }
            }
        }
    }
}

@Composable
fun MagnifierDemo(magnifierActive: Boolean, magnifierActiveChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Switch(checked = magnifierActive, onCheckedChange = magnifierActiveChange)
        Text(stringResource(R.string.magnifier))
    }
}
