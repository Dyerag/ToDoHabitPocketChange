package com.example.todohabitpocketchange;

import java.time.LocalDate;

public class TodoItem {
    private String name;
    private boolean isCompleted;
    private float reward;
    private LocalDate Created;

    public TodoItem(String name, float reward) {
        this.name = name;
        this.isCompleted = false;
        this.reward = reward;
        this.Created = LocalDate.now();
    }

    public String getName() {
        return name;
    }

    public float getReward() {
        return reward;
    }

    public boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDate getCreated() {
        return Created;
    }
}