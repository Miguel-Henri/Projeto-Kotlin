package br.edu.ifsp.list4u

import android.app.Application
import androidx.room.Room
import br.edu.ifsp.list4u.data.local.List4UDatabase
import br.edu.ifsp.list4u.data.local.MIGRATION_1_2
import br.edu.ifsp.list4u.data.repository.TodoRepository

class TodoApp : Application() {
    val database by lazy {
        Room.databaseBuilder(this, List4UDatabase::class.java, "list4u_db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    val repository by lazy {
        TodoRepository(database.todoListDao(), database.todoItemDao())
    }
}