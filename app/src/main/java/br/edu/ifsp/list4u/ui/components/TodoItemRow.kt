package br.edu.ifsp.list4u.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity

@Composable
fun TodoItemRow(
    item: TodoItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.isCompleted, onCheckedChange = { onToggle() })
        Text(item.name, modifier = Modifier.weight(1f))
        TextButton(onClick = onDelete) { Text("Excluir") }
    }
}
