package com.example.todohabitpocketchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends ArrayAdapter<TodoItem> {
    private OnTodoChangedListener listener;

    public TodoAdapter(Context context, List<TodoItem> todos, OnTodoChangedListener listener) {
        super(context, 0, todos);
        this.listener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_todo, parent, false);
        }

        TextView textTitle = convertView.findViewById(R.id.name);
        TextView reward = convertView.findViewById(R.id.reward);
        TextView createdDate = convertView.findViewById(R.id.creationDate);
        CheckBox completed = convertView.findViewById(R.id.completionCheck);
        Button deleteBtn = convertView.findViewById(R.id.deleteBtn);

        textTitle.setText(todo.getName());
        reward.setText("Reward: " + todo.getReward() + " kr.");
        createdDate.setText(todo.getCreated().toString());

        completed.setOnCheckedChangeListener(null);
        completed.setChecked(todo.getCompleted());

        completed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleCompleted(todo);
            }
        });

        deleteBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(todo);
            }
        });

        return convertView;
    }


    private ArrayList<TodoItem> getAllItems() {
        ArrayList<TodoItem> items = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            items.add(getItem(i));
        }
        return items;
    }
}
