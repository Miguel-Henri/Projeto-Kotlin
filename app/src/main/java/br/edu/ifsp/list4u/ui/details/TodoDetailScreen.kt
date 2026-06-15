package br.edu.ifsp.list4u.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import br.edu.ifsp.list4u.ui.AppViewModelProvider
import br.edu.ifsp.list4u.ui.components.TodoItemRow

@Composable
fun TodoDetailScreen(
    list: TodoListEntity,
    onBack: () -> Unit,
    viewModel: TodoDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val todoItems by viewModel.items(list.id).collectAsState(initial = emptyList())
    var state by remember { mutableStateOf(TodoDetailState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 48.dp)
    ) {
        Button(onClick = onBack) { Text("<-") }
        Text(list.name, style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(todoItems) { item ->
                TodoItemRow(
                    item = item,
                    onToggle = { viewModel.toggle(item) },
                    onDelete = { viewModel.deleteItem(item) }
                )
            }
        }

        if (state.isAdding) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = state.newItemName,
                    onValueChange = { state = state.copy(newItemName = it) },
                    label = { Text("Digite o Nome do Item") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (state.newItemName.isNotBlank()) {
                            viewModel.addItem(state.newItemName.trim(), list.id)
                        }
                        state = TodoDetailState()
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("OK")
                }
            }
        } else {
            Button(
                onClick = { state = state.copy(isAdding = true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Novo Item")
            }
        }
    }
}
