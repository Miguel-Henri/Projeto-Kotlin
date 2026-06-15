package br.edu.ifsp.list4u.ui.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import br.edu.ifsp.list4u.data.repository.TodoRepository
import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val lists = repository.getAllLists()

    fun totalItems(listId: Long) = repository.getTotalItems(listId)

    fun completedItems(listId: Long) = repository.getCompletedItems(listId)

    fun addList(name: String) {
        viewModelScope.launch { repository.insertList(name) }
    }

    fun deleteList(list: TodoListEntity) {
        viewModelScope.launch { repository.deleteList(list) }
    }
}
