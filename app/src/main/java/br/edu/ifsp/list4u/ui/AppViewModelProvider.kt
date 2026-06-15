package br.edu.ifsp.list4u.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.edu.ifsp.list4u.TodoApp
import br.edu.ifsp.list4u.ui.details.TodoDetailViewModel
import br.edu.ifsp.list4u.ui.lists.TodoListViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            TodoListViewModel((this[APPLICATION_KEY] as TodoApp).repository)
        }
        initializer {
            TodoDetailViewModel((this[APPLICATION_KEY] as TodoApp).repository)
        }
    }
}
