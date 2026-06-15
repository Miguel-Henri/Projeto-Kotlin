package br.edu.ifsp.list4u.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.data.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoDetailViewModel(private val repository: TodoRepository) : ViewModel() {

    fun items(listId: Long) = repository.getItemsByList(listId)

    fun addItem(name: String, listId: Long, dueDate: Long? = null) {
        viewModelScope.launch { repository.insertItem(name, listId, dueDate) }
    }

    fun toggle(item: TodoItemEntity) {
        viewModelScope.launch { repository.updateItem(item.copy(isCompleted = !item.isCompleted)) }
    }

    fun deleteItem(item: TodoItemEntity) {
        viewModelScope.launch { repository.deleteItem(item) }
    }

    fun reorder(items: List<TodoItemEntity>) {
        viewModelScope.launch { repository.reorderItems(items) }
    }
}