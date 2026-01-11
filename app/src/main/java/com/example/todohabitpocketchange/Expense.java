package com.example.todohabitpocketchange;

import java.time.LocalDate;

public class Expense {
    private String name;
    private float cost;
    private LocalDate date;

    public Expense(String name, float cost) {
        this.name = name;
        this.cost = cost;
        this.date = LocalDate.now();
    }

    public String getName() { return name; }
    public float getCost() { return cost; }
    public LocalDate getDate() { return date; }
}

