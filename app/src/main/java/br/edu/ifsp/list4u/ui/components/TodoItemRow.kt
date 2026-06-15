package br.edu.ifsp.list4u.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.ui.theme.DestructRed
import br.edu.ifsp.list4u.ui.theme.DividerColor
import br.edu.ifsp.list4u.ui.theme.TextSecond
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TodoItemRow(
    item: TodoItemEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFmt  = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val now      = remember { System.currentTimeMillis() }
    val isOverdue = item.dueDate != null && item.dueDate < now && !item.isCompleted

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor   = MaterialTheme.colorScheme.primary,
                    uncheckedColor = if (isOverdue) DestructRed else DividerColor,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = if (item.isCompleted) TextDecoration.LineThrough
                        else TextDecoration.None
                    ),
                    color = if (item.isCompleted) TextSecond
                    else MaterialTheme.colorScheme.onBackground
                )
                if (item.dueDate != null) {
                    Text(
                        text = if (isOverdue) "⚠ Vencido · ${dateFmt.format(Date(item.dueDate))}"
                        else "Prazo: ${dateFmt.format(Date(item.dueDate))}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOverdue) DestructRed else TextSecond,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            TextButton(onClick = onDelete) {
                Text("Excluir", color = DestructRed, style = MaterialTheme.typography.labelSmall)
            }
        }
        HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
    }
}