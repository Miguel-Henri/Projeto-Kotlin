package br.edu.ifsp.list4u.data.repository

import br.edu.ifsp.list4u.data.local.dao.TodoItemDao
import br.edu.ifsp.list4u.data.local.dao.TodoListDao
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import kotlinx.coroutines.flow.Flow

class TodoRepository(
    private val todoListDao: TodoListDao,
    private val todoItemDao: TodoItemDao
) {
    fun getAllLists(): Flow<List<TodoListEntity>> = todoListDao.getAllLists()
    suspend fun insertList(name: String) = todoListDao.insertList(TodoListEntity(name = name))
    suspend fun deleteList(list: TodoListEntity) = todoListDao.deleteList(list)
    fun getTotalItems(listId: Long) = todoListDao.getTotalItems(listId)
    fun getCompletedItems(listId: Long) = todoListDao.getCompletedItems(listId)

    fun getItemsByList(listId: Long): Flow<List<TodoItemEntity>> =
        todoItemDao.getItemsByList(listId)

    suspend fun insertItem(name: String, listId: Long, dueDate: Long?): Long {
        val order = todoItemDao.nextSortOrder(listId)
        return todoItemDao.insertItem(
            TodoItemEntity(name = name, listId = listId, sortOrder = order, dueDate = dueDate)
        )
    }

    suspend fun updateItem(item: TodoItemEntity) = todoItemDao.updateItem(item)

    suspend fun reorderItems(items: List<TodoItemEntity>) {
        val reindexed = items.mapIndexed { index, item -> item.copy(sortOrder = index) }
        todoItemDao.updateItems(reindexed)
    }

    suspend fun deleteItem(item: TodoItemEntity) = todoItemDao.deleteItem(item)
}