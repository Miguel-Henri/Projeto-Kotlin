package br.edu.ifsp.list4u.ui.lists

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
import br.edu.ifsp.list4u.ui.components.TodoListRow

@Composable
fun TodoListScreen(
    onListClick: (TodoListEntity) -> Unit,
    viewModel: TodoListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val lists by viewModel.lists.collectAsState(initial = emptyList())
    var state by remember { mutableStateOf(TodoListState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 48.dp)
    ) {
        Text("Minhas listas", style = MaterialTheme.typography.titleLarge)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(lists) { list ->
                val total by viewModel.totalItems(list.id).collectAsState(initial = 0)
                val completed by viewModel.completedItems(list.id).collectAsState(initial = 0)
                TodoListRow(
                    list = list,
                    completed = completed,
                    total = total,
                    onClick = { onListClick(list) },
                    onDelete = { viewModel.deleteList(list) }
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
                    value = state.newListName,
                    onValueChange = { state = state.copy(newListName = it) },
                    label = { Text("Nome da Lista") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (state.newListName.isNotBlank()) {
                            viewModel.addList(state.newListName.trim())
                        }
                        state = TodoListState()
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
                Text("Nova Lista")
            }
        }
    }
}
