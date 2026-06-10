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

    suspend fun insertList(name: String): Long =
        todoListDao.insertList(TodoListEntity(name = name))

    suspend fun deleteList(list: TodoListEntity) = todoListDao.deleteList(list)

    fun getTotalItems(listId: Long): Flow<Int> = todoListDao.getTotalItems(listId)

    fun getCompletedItems(listId: Long): Flow<Int> = todoListDao.getCompletedItems(listId)

    fun getItemsByList(listId: Long): Flow<List<TodoItemEntity>> =
        todoItemDao.getItemsByList(listId)

    suspend fun insertItem(name: String, listId: Long): Long =
        todoItemDao.insertItem(TodoItemEntity(name = name, listId = listId))

    suspend fun updateItem(item: TodoItemEntity) = todoItemDao.updateItem(item)

    suspend fun deleteItem(item: TodoItemEntity) = todoItemDao.deleteItem(item)
}
