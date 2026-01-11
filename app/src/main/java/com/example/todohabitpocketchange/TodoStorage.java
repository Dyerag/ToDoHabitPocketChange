package com.example.todohabitpocketchange;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TodoStorage {
    private static final String PREFS_NAME = "todo_prefs";
    private static final String KEY_UNFINISHED = "unfinished_todos";
    private static final String KEY_FINISHED = "finished_todos";

    public static void save(
            Context context,
            ArrayList<TodoItem> unfinished,
            ArrayList<TodoItem> finished
    ) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        editor.putString(KEY_UNFINISHED, gson.toJson(unfinished));
        editor.putString(KEY_FINISHED, gson.toJson(finished));
        editor.apply();
    }

    public static ArrayList<TodoItem> loadUnfinished(Context context) {
        return loadList(context, KEY_UNFINISHED);
    }

    public static ArrayList<TodoItem> loadFinished(Context context) {
        return loadList(context, KEY_FINISHED);
    }

    private static ArrayList<TodoItem> loadList(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(key, null);

        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<ArrayList<TodoItem>>() {}.getType();
        ArrayList<TodoItem> list = new Gson().fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }
}
