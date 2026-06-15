package br.edu.ifsp.list4u.ui.details

data class TodoDetailState(
    val isAdding: Boolean = false,
    val newItemName: String = "",
    val newItemDueDate: Long? = null,
    val showDatePicker: Boolean = false
)