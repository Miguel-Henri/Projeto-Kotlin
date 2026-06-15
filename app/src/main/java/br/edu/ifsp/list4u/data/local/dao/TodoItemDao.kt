package br.edu.ifsp.list4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM todo_item WHERE list_id = :listId ORDER BY sort_order ASC, id ASC")
    fun getItemsByList(listId: Long): Flow<List<TodoItemEntity>>

    @Insert
    suspend fun insertItem(item: TodoItemEntity): Long

    @Update
    suspend fun updateItem(item: TodoItemEntity)

    @Update
    suspend fun updateItems(items: List<TodoItemEntity>)

    @Delete
    suspend fun deleteItem(item: TodoItemEntity)

    @Query("SELECT COALESCE(MAX(sort_order), -1) + 1 FROM todo_item WHERE list_id = :listId")
    suspend fun nextSortOrder(listId: Long): Int
}