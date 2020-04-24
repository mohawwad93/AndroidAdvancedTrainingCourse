package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.AppExecutor;
import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.Task;

public class AddTaskViewModel extends ViewModel {

    private AppDatabase mAppDatabase;
    private LiveData<Task> taskLiveData;

    public AddTaskViewModel(Application application, int taskId){
        mAppDatabase = AppDatabase.getInstance(application);
        taskLiveData = mAppDatabase.getTaskDao().loadTask(taskId);
    }


    public LiveData<Task> getTaskLiveData(){
        return taskLiveData;
    }

    public void insert(final Task task){
        AppExecutor.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.getTaskDao().insert(task);
            }
        });
    }

    public void update(final Task task){
        AppExecutor.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.getTaskDao().update(task);
            }
        });
    }
}
