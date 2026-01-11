package com.example.todohabitpocketchange;

public interface OnTodoChangedListener {
    void onToggleCompleted(TodoItem todo);
    void onDelete(TodoItem todo);
}

