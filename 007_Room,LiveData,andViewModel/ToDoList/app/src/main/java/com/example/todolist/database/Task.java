package com.example.todolist.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mytasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @ColumnInfo(name = "datetime")
    private long dateMillisecond;
    private int priority;

    @Ignore
    public Task(String name, long dateMillisecond, int priority) {
        this.name = name;
        this.dateMillisecond = dateMillisecond;
        this.priority = priority;
    }

    public Task(int id, String name, long dateMillisecond, int priority) {
        this.id = id;
        this.name = name;
        this.dateMillisecond = dateMillisecond;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateMillisecond() {
        return dateMillisecond;
    }

    public void setDateMillisecond(long dateMillisecond) {
        this.dateMillisecond = dateMillisecond;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
