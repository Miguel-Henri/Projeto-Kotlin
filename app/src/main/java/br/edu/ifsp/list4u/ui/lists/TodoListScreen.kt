package br.edu.ifsp.list4u.ui.lists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import br.edu.ifsp.list4u.ui.AppViewModelProvider
import br.edu.ifsp.list4u.ui.components.TodoListRow
import br.edu.ifsp.list4u.ui.theme.DividerColor
import br.edu.ifsp.list4u.ui.theme.TextSecond

@Composable
fun TodoListScreen(
    onListClick: (TodoListEntity) -> Unit,
    viewModel: TodoListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val lists by viewModel.lists.collectAsState(initial = emptyList())
    var state by remember { mutableStateOf(TodoListState()) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLists = remember(lists, searchQuery) {
        if (searchQuery.isBlank()) lists
        else lists.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 52.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Text(
            text = "Minhas Listas",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar lista...", color = TextSecond) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecond) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = DividerColor,
            )
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            if (filteredLists.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (searchQuery.isBlank()) "🗂️" else "🔍",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (searchQuery.isBlank()) "Nenhuma lista ainda"
                                else "Nenhum resultado para \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecond,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (searchQuery.isBlank()) "Crie sua primeira lista abaixo"
                                else "Tente um nome diferente",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecond,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredLists) { list ->
                    val total     by viewModel.totalItems(list.id).collectAsState(initial = 0)
                    val completed by viewModel.completedItems(list.id).collectAsState(initial = 0)
                    TodoListRow(
                        list      = list,
                        completed = completed,
                        total     = total,
                        onClick   = { onListClick(list) },
                        onDelete  = { viewModel.deleteList(list) }
                    )
                }
            }
        }

        if (state.isAdding) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = state.newListName,
                    onValueChange = { state = state.copy(newListName = it) },
                    label = { Text("Nome da lista", color = TextSecond) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = DividerColor,
                    )
                )
                Button(
                    onClick = {
                        if (state.newListName.isNotBlank()) viewModel.addList(state.newListName.trim())
                        state = TodoListState()
                    },
                    modifier = Modifier.padding(start = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("OK")
                }
            }
        } else {
            Button(
                onClick = { state = state.copy(isAdding = true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("+ Nova Lista")
            }
        }
    }
}