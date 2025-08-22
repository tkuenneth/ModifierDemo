package dev.tkuenneth.modifierdemo

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.magnifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler

fun Modifier.draggableMagnifier(enabled: Boolean): Modifier = composed {
    if (enabled) {
        var touchPosition by remember { mutableStateOf(Offset.Unspecified) }
        pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { touchPosition = it },
                onDrag = { _, delta -> touchPosition += delta },
                onDragEnd = { touchPosition = Offset.Unspecified },
                onDragCancel = { touchPosition = Offset.Unspecified }
            )
        }.magnifier(
            sourceCenter = { touchPosition },
            magnifierCenter = { touchPosition },
            zoom = 4F
        )
    } else {
        Modifier
    }
}

fun Modifier.openUrl(
    url: String,
    onFailure: (Throwable) -> Unit = {}
): Modifier =
    this then OpenUrlModifierNodeElement(
        url = url,
        onFailure = onFailure
    )

private data class OpenUrlModifierNodeElement(
    val url: String,
    val onFailure: (Throwable) -> Unit
) : ModifierNodeElement<OpenUrlModifierNode>() {
    override fun create() = OpenUrlModifierNode(
        initialUrl = url,
        onFailure = onFailure
    )

    override fun update(node: OpenUrlModifierNode) {
        node.url = url
        node.onFailure = onFailure
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "openUrl"
        properties["url"] = url
        properties["onFailure"] = onFailure
    }
}

private class OpenUrlModifierNode(
    initialUrl: String,
    var onFailure: (Throwable) -> Unit
) : DelegatingNode(), CompositionLocalConsumerModifierNode {

    private val pointerInputNode = SuspendingPointerInputModifierNode {
        detectTapGestures {
            runCatching {
                uriHandler?.openUri(url)
            }.onFailure { onFailure(it) }
        }
    }

    var url: String = initialUrl
        set(value) {
            if (field != value) {
                field = value
                pointerInputNode.resetPointerInputHandler()
            }
        }

    private val uriHandler: UriHandler?
        get() = currentValueOf(LocalUriHandler)

    init {
        delegate(pointerInputNode)
    }
}

inline fun Modifier.conditional(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        then(block(Modifier))
    } else {
        this
    }
}
