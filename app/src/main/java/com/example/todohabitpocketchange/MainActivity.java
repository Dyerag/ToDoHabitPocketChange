package com.example.todohabitpocketchange;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

        TextView unfinishedHeader = findViewById(R.id.unfinishedHeader);
        TextView finishedHeader = findViewById(R.id.finishedHeader);

        ListView unfinishedListView = findViewById(R.id.unfinishedList);
        ListView finishedListView = findViewById(R.id.finishedList);
        FloatingActionButton febAdd = findViewById(R.id.fabAdd);

        unfinishedHeader.setOnClickListener(v -> {
            if (unfinishedListView.getVisibility() == View.VISIBLE) {
                unfinishedListView.setVisibility(View.GONE);
                unfinishedHeader.setText("Unfinished Tasks ▶");
            } else {
                unfinishedListView.setVisibility(View.VISIBLE);
                unfinishedHeader.setText("Unfinished Tasks ▼");
            }
        });

        finishedHeader.setOnClickListener(v -> {
            if (finishedListView.getVisibility() == View.VISIBLE) {
                finishedListView.setVisibility(View.GONE);
                finishedHeader.setText("Finished Tasks ▶");
            } else {
                finishedListView.setVisibility(View.VISIBLE);
                finishedHeader.setText("Finished Tasks ▼");
            }
        });

        unfinishedTodos = TodoStorage.loadUnfinished(this);
        finishedTodos = TodoStorage.loadFinished(this);

        unfinishedAdapter = new TodoAdapter(
                this,
                unfinishedTodos,
                new OnTodoChangedListener() {
                    @Override
                    public void onToggleCompleted(TodoItem todo) {
                        unfinishedTodos.remove(todo);
                        todo.setCompleted(true);
                        finishedTodos.add(todo);
                        MoneyStorage.save(MainActivity.this, todo.getReward());
                        saveTodos();
                    }

                    @Override
                    public void onDelete(TodoItem todo) {
                        unfinishedTodos.remove(todo);
                        saveTodos();
                    }
                }
        );

        finishedAdapter = new TodoAdapter(
                this,
                finishedTodos,
                new OnTodoChangedListener() {
                    @Override
                    public void onToggleCompleted(TodoItem todo) {
                        finishedTodos.remove(todo);
                        todo.setCompleted(false);
                        unfinishedTodos.add(todo);
                        MoneyStorage.save(MainActivity.this, -todo.getReward());
                        saveTodos();
                    }

                    @Override
                    public void onDelete(TodoItem todo) {
                        finishedTodos.remove(todo);
                        saveTodos();
                    }
                }
        );

        unfinishedListView.setAdapter(unfinishedAdapter);
        finishedListView.setAdapter(finishedAdapter);

        addTodoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        String title = result.getData().getStringExtra(AddTodoActivity.EXTRA_TITLE);
                        String reward = result.getData().getStringExtra(AddTodoActivity.EXTRA_REWARD);

                        if (title != null && !title.isEmpty()
                                && reward != null && !reward.isEmpty()) {

                            TodoItem todo = new TodoItem(
                                    title.trim(),
                                    Float.parseFloat(reward.trim())
                            );

                            todo.setCompleted(false);
                            unfinishedTodos.add(todo);
                            saveTodos();
                        }
                    }
                }
        );

        febAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
            addTodoLauncher.launch(intent);
        });

    }

    private void saveTodos() {
        unfinishedAdapter.notifyDataSetChanged();
        finishedAdapter.notifyDataSetChanged();
        TodoStorage.save(this, unfinishedTodos, finishedTodos);
    }
}