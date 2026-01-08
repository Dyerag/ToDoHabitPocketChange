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
    public TodoAdapter(@NonNull Context context, List<TodoItem> todos) {
        super(context, 0, todos);
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
        reward.setText(String.valueOf(todo.getReward()));
        createdDate.setText(todo.getCreated().toString());
        completed.setChecked(todo.getCompleted());

        completed.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setCompleted(isChecked);
            notifyDataSetChanged();
            MoneyStorage.save(getContext(), isChecked ? todo.getReward() : -todo.getReward());
            TodoStorage.save(getContext(), new ArrayList<>(getAllItems()));
        });

        deleteBtn.setOnClickListener(v -> {
            remove(todo);
            notifyDataSetChanged();
            TodoStorage.save(getContext(), new ArrayList<>(getAllItems()));
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
