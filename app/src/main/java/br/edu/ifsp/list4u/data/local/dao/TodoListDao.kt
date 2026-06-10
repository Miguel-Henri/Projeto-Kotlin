package br.edu.ifsp.list4u.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_list ORDER BY name ASC")
    fun getAllLists(): Flow<List<TodoListEntity>>

    @Insert
    suspend fun insertList(list: TodoListEntity): Long

    @Delete
    suspend fun deleteList(list: TodoListEntity)

    @Query("SELECT COUNT(*) FROM todo_item WHERE list_id = :listId")
    fun getTotalItems(listId: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM todo_item WHERE list_id = :listId AND is_completed = 1")
    fun getCompletedItems(listId: Long): Flow<Int>
}
