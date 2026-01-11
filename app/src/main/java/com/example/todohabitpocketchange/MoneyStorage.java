package com.example.todohabitpocketchange;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MoneyStorage {
    private static final String PREFS_NAME = "money_prefs";
    private static final String KEY_BALANCE = "money";
    private static final String KEY_EXPENSES = "expenses";

    public static void save(Context context, float balanceChange) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String json = new Gson().toJson(load(context) + balanceChange);
        editor.putString(KEY_BALANCE, json);
        editor.apply();
    }

    public static float load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_BALANCE, null);

        if (json == null) return (float) 0;

        return new Gson().fromJson(json, float.class);
    }

    public static void saveExpense(Context context, Expense expense) {
        ArrayList<Expense> expenses = loadExpenses(context);
        expenses.add(expense);

        String json = new Gson().toJson(expenses);
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_EXPENSES, json)
                .apply();
    }

    public static ArrayList<Expense> loadExpenses(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_EXPENSES, null);
        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<ArrayList<Expense>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
}
