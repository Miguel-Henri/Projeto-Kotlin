package br.edu.ifsp.list4u.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.edu.ifsp.list4u.data.local.dao.TodoItemDao
import br.edu.ifsp.list4u.data.local.dao.TodoListDao
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity

@Database(
    entities = [TodoListEntity::class, TodoItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class List4UDatabase : RoomDatabase() {
    abstract fun todoListDao(): TodoListDao
    abstract fun todoItemDao(): TodoItemDao
}
