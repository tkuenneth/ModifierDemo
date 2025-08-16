package dev.tkuenneth.modifierdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.contextmenu.builder.item
import androidx.compose.foundation.text.contextmenu.modifier.appendTextContextMenuComponents
import androidx.compose.foundation.text.contextmenu.modifier.filterTextContextMenuComponents
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalContext
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
            modifier = Modifier.fillMaxSize(), topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) })
            }) { innerPadding ->
            var magnifierActive by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .draggableMagnifier(enabled = magnifierActive)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.align(Alignment.Start)) {
                    MagnifierDemo(magnifierActive = magnifierActive) { magnifierActive = it }
                    KeepScreenOnDemo()
                }
                UriHandlerDemo()
                ContextMenuDemo(stringResource(R.string.context_menu))
                GraphicsLayerDemo()
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
fun UriHandlerDemo() {
    val uriHandler = LocalUriHandler.current
    val url = stringResource(R.string.url)
    Text(
        text = url, modifier = Modifier.clickable {
//                try {
//                    uriHandler.openUri(url)
//                } catch (_: IllegalArgumentException) {
//                    // Do something
//                }
            runCatching {
                uriHandler.openUri(url)
            }.onFailure {
                // Do something
            }
        })
}

@Composable
fun ContextMenuDemo(text: String) {
    val state = rememberTextFieldState(text)
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(all = 8.dp)
    ) {
        BasicTextField(
            state = state,
            modifier = Modifier
                .appendTextContextMenuComponents {
                    this.item(
                        key = ContextMenuComponentsKey.Clear,
                        label = context.getString(R.string.clear)
                    ) {
                        state.clearText()
                        close()
                    }
                }
                .filterTextContextMenuComponents { component ->
                    if (component.key == ContextMenuComponentsKey.Clear) state.text.isNotEmpty() else true
                },
        )
    }
}

sealed class ContextMenuComponentsKey {
    data object Clear : ContextMenuComponentsKey()
}

@Composable
fun WithReflection(
    modifier: Modifier, content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
        Box(modifier = modifier) {
            content()
        }
    }
}

@Composable
fun GraphicsLayerDemo() {
    WithReflection(
        modifier = Modifier.graphicsLayer {
            scaleX = 1.08f
            scaleY = -1.1f
            alpha = 0.3f
            rotationX = 45F
            translationY = -14F
        }) { TextWithMarquee() }
}

@Composable
fun TextWithMarquee() {
    Text(
        text = stringResource(R.string.pangram),
        modifier = Modifier
            .width(200.dp)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer)
            .basicMarquee(),
        style = MaterialTheme.typography.headlineLarge
    )
}
