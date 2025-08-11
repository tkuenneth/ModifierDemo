package dev.tkuenneth.modifierdemo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwitchWithText(
    active: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onActiveChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.clickable { onActiveChange(!active) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Switch(checked = active, onCheckedChange = onActiveChange)
        Text(text)
    }
}
