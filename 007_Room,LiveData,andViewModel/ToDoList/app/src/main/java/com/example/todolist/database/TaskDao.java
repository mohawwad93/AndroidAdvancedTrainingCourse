package com.example.todolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM mytasks")
    LiveData<List<Task>> loadAllTasks();

    @Query("SELECT * FROM mytasks WHERE id=:id")
    LiveData<Task> loadTask(int id);
}
