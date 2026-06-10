package br.edu.ifsp.list4u

import android.app.Application
import androidx.room.Room
import br.edu.ifsp.list4u.data.local.List4UDatabase
import br.edu.ifsp.list4u.data.repository.TodoRepository

class TodoApp : Application() {

    lateinit var database: List4UDatabase
        private set

    lateinit var repository: TodoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            List4UDatabase::class.java,
            "list4u.db"
        ).build()

        repository = TodoRepository(
            todoListDao = database.todoListDao(),
            todoItemDao = database.todoItemDao()
        )
    }
}
