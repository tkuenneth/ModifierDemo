package dev.tkuenneth.modifierdemo

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.magnifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.draggableMagnifier(enabled: Boolean): Modifier = composed {
    if (enabled) {
        var touchPosition by remember { mutableStateOf(Offset.Unspecified) }
        this
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
}
