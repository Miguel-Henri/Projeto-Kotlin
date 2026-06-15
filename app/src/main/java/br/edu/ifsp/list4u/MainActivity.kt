package br.edu.ifsp.list4u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import br.edu.ifsp.list4u.ui.details.TodoDetailScreen
import br.edu.ifsp.list4u.ui.lists.TodoListScreen
import br.edu.ifsp.list4u.ui.theme.List4UTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            List4UTheme {
                var selectedList by remember { mutableStateOf<TodoListEntity?>(null) }
                val current = selectedList
                if (current == null) {
                    TodoListScreen(onListClick = { selectedList = it })
                } else {
                    TodoDetailScreen(list = current, onBack = { selectedList = null })
                }
            }
        }
    }
}