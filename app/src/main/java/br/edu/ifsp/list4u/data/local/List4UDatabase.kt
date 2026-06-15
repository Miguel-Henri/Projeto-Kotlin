package br.edu.ifsp.list4u.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.edu.ifsp.list4u.data.local.dao.TodoItemDao
import br.edu.ifsp.list4u.data.local.dao.TodoListDao
import br.edu.ifsp.list4u.data.local.entity.TodoItemEntity
import br.edu.ifsp.list4u.data.local.entity.TodoListEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE todo_item ADD COLUMN sort_order INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE todo_item ADD COLUMN due_date INTEGER DEFAULT NULL")
    }
}

@Database(
    entities = [TodoListEntity::class, TodoItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class List4UDatabase : RoomDatabase() {
    abstract fun todoListDao(): TodoListDao
    abstract fun todoItemDao(): TodoItemDao
}