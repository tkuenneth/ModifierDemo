package dev.tkuenneth.modifierdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalUriHandler
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
            Column(
                modifier = Modifier
                    .draggableMagnifier(enabled = magnifierActive)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MagnifierDemo(magnifierActive = magnifierActive) { magnifierActive = it }
                KeepScreenOnDemo()
                UriHandlerDemo()
                ContextMenuDemo()
            }
        }
    }
}

@Composable
fun MagnifierDemo(magnifierActive: Boolean, magnifierActiveChange: (Boolean) -> Unit) {
    SwitchWithText(
        active = magnifierActive,
        text = stringResource(R.string.magnifier),
        onActiveChange = magnifierActiveChange
    )
}

@Composable
fun KeepScreenOnDemo() {
    var keepScreenOnActive by remember { mutableStateOf(false) }
    SwitchWithText(
        active = keepScreenOnActive,
        text = stringResource(R.string.keep_screen_on),
        modifier = if (keepScreenOnActive) Modifier.keepScreenOn() else Modifier
    ) {
        keepScreenOnActive = it
    }
}

@Composable
fun ColumnScope.UriHandlerDemo() {
    val uriHandler = LocalUriHandler.current
    val url = stringResource(R.string.url)
    Text(
        text = url,
        modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally)
            .clickable {
                try {
                    uriHandler.openUri(url)
                } catch (_: IllegalArgumentException) {
                    // Do something
                }
            }
    )
}

@Composable
fun ColumnScope.ContextMenuDemo() {
//    Text(
//        text = stringResource(R.string.context_menu),
//        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
//            .contextDe {
//
//            }
//    )
}
