package br.edu.ifsp.list4u.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import br.edu.ifsp.list4u.ui.AppViewModelProvider
import br.edu.ifsp.list4u.ui.components.TodoItemRow
import br.edu.ifsp.list4u.ui.theme.DividerColor
import br.edu.ifsp.list4u.ui.theme.TextSecond
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    list: TodoListEntity,
    onBack: () -> Unit,
    viewModel: TodoDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val todoItems by viewModel.items(list.id).collectAsState(initial = emptyList())
    var state by remember { mutableStateOf(TodoDetailState()) }
    var searchQuery by remember { mutableStateOf("") }

    // drag & drop state
    val listState = rememberLazyListState()
    var dragIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var draggableItems by remember { mutableStateOf(emptyList<TodoItemEntity>()) }

    if (dragIndex == -1) draggableItems = todoItems

    val filteredItems = remember(draggableItems, searchQuery) {
        if (searchQuery.isBlank()) draggableItems
        else draggableItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val completed = todoItems.count { it.isCompleted }
    val total     = todoItems.size
    val progress  = if (total > 0) completed.toFloat() / total else 0f

    val dateFmt = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val datePickerState = rememberDatePickerState()

    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { state = state.copy(showDatePicker = false) },
            confirmButton = {
                TextButton(onClick = {
                    state = state.copy(
                        newItemDueDate = datePickerState.selectedDateMillis,
                        showDatePicker = false
                    )
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { state = state.copy(showDatePicker = false) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 52.dp, end = 24.dp, bottom = 32.dp)
    ) {

        Text(
            text = list.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(99.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = DividerColor
            )
            Text(
                text = "$completed/$total feitos",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecond
            )
        }


        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar item...", color = TextSecond) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecond) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = DividerColor,
            )
        )


        when {
            todoItems.isEmpty() -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum item ainda",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecond,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Adicione o primeiro item abaixo",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecond,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            filteredItems.isEmpty() -> {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum resultado para \"$searchQuery\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecond,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tente um nome diferente",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecond,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(
                        items = filteredItems,
                        key   = { _, item -> item.id }
                    ) { index, item ->
                        val isDragging = index == dragIndex
                        Box(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationY    = if (isDragging) dragOffsetY else 0f
                                    shadowElevation = if (isDragging) 8f else 0f
                                }
                                .background(
                                    if (isDragging) MaterialTheme.colorScheme.surfaceVariant
                                    else MaterialTheme.colorScheme.background
                                )
                                .pointerInput(draggableItems) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = { dragIndex = index; dragOffsetY = 0f },
                                        onDrag = { _, dragAmount ->
                                            dragOffsetY += dragAmount.y
                                            val itemHeightPx = 72.dp.toPx()
                                            val rawTarget = index + (dragOffsetY / itemHeightPx).toInt()
                                            val target = rawTarget.coerceIn(0, draggableItems.lastIndex)
                                            if (target != dragIndex) {
                                                val mutable = draggableItems.toMutableList()
                                                val moved   = mutable.removeAt(dragIndex)
                                                mutable.add(target, moved)
                                                draggableItems = mutable
                                                dragOffsetY -= (target - dragIndex) * itemHeightPx
                                                dragIndex = target
                                            }
                                        },
                                        onDragEnd    = {
                                            viewModel.reorder(draggableItems)
                                            dragIndex  = -1
                                            dragOffsetY = 0f
                                        },
                                        onDragCancel = { dragIndex = -1; dragOffsetY = 0f }
                                    )
                                }
                        ) {
                            TodoItemRow(
                                item     = item,
                                onToggle = { viewModel.toggle(item) },
                                onDelete = { viewModel.deleteItem(item) }
                            )
                        }
                    }
                }
            }
        }

        if (state.isAdding) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.newItemName,
                        onValueChange = { state = state.copy(newItemName = it) },
                        label = { Text("Nome do item", color = TextSecond) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = DividerColor,
                        )
                    )
                    Button(
                        onClick = {
                            if (state.newItemName.isNotBlank()) {
                                viewModel.addItem(state.newItemName.trim(), list.id, state.newItemDueDate)
                            }
                            state = TodoDetailState()
                        },
                        modifier = Modifier.padding(start = 10.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("OK")
                    }
                }
                val dueDateLabel = state.newItemDueDate
                    ?.let { "Prazo: ${dateFmt.format(Date(it))}" }
                    ?: "Definir prazo (opcional)"
                TextButton(onClick = { state = state.copy(showDatePicker = true) }) {
                    Text(dueDateLabel, style = MaterialTheme.typography.labelSmall, color = TextSecond)
                }
            }
        } else {
            Button(
                onClick = { state = state.copy(isAdding = true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("+ Novo Item")
            }
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor)
        ) {
            Text("← Voltar", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}