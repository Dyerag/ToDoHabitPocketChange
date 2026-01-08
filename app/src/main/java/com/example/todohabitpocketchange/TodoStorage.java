package com.example.todohabitpocketchange;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TodoStorage {
    private static final String PREFS_NAME = "todo_prefs";
    private static final String KEY = "todos";

    public static void save(Context context, ArrayList<TodoItem> todos) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(todos);
        editor.putString(KEY, json);
        editor.apply();
    }

    public static ArrayList<TodoItem> load(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY, null);

        if (json == null) return  new ArrayList<>();
        Type type = new TypeToken<ArrayList<TodoItem>>() {}.getType();
        ArrayList<TodoItem> todos = new Gson().fromJson(json, type);

        return (todos != null) ? todos : new ArrayList<>();
    }
}
