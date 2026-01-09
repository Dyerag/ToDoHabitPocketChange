package com.example.todohabitpocketchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.internal.NumberLimits;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TodoItem> todoList;
    private ArrayList<TodoItem> unfinishedTodos;
    private ArrayList<TodoItem> finishedTodos;
    private TodoAdapter unfinishedAdapter;
    private TodoAdapter finishedAdapter;
    private ActivityResultLauncher<Intent> addTodoLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView unfinishedListView = findViewById(R.id.unfinishedList);
        ListView finishedListView = findViewById(R.id.finishedList);
        FloatingActionButton febAdd = findViewById(R.id.fabAdd);

        todoList = TodoStorage.load(this);

        unfinishedTodos = new ArrayList<>();
        finishedTodos = new ArrayList<>();

        sortTodoList();

        unfinishedAdapter = new TodoAdapter(this, unfinishedTodos, () -> {
            sortTodoList();
            TodoStorage.save(this, todoList);
        });

        finishedAdapter = new TodoAdapter(this, finishedTodos, ()->{
            sortTodoList();
            TodoStorage.save(this,todoList);
        });

        unfinishedListView.setAdapter(unfinishedAdapter);
        finishedListView.setAdapter(finishedAdapter);

        addTodoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String title = result.getData().getStringExtra(AddTodoActivity.EXTRA_TITLE);
                        String reward = result.getData().getStringExtra(AddTodoActivity.EXTRA_REWARD);

                        if (title != null && !title.trim().isEmpty() &&
                                reward != null && !reward.trim().isEmpty()) {
                            todoList.add(new TodoItem(title.trim(), Float.parseFloat(reward.trim())));
                            sortTodoList();
                            TodoStorage.save(this, todoList);
                        }
                    }
                }
        );

        febAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
            addTodoLauncher.launch(intent);
        });

    }

    private void sortTodoList() {
        finishedTodos.clear();
        unfinishedTodos.clear();

        for (TodoItem todo : todoList) {
            if (todo.getCompleted())
                finishedTodos.add(todo);
            else
                unfinishedTodos.add(todo);
        }

        if (unfinishedAdapter != null) {
            unfinishedAdapter.notifyDataSetChanged();
        }

        if (finishedAdapter != null) {
            finishedAdapter.notifyDataSetChanged();
        }
    }
}