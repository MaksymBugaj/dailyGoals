package com.example.maksy.dailygoals.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "goal_table")
public class Goal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String description;

    private int priority;

    public Goal(String description, int priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
