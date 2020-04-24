package com.example.todolist.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolist.AppExecutor;
import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.Task;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private LiveData<List<Task>> mListLiveData;


    public MainViewModel(Application application){

        mAppDatabase = AppDatabase.getInstance(application);
        mListLiveData = mAppDatabase.getTaskDao().loadAllTasks();
    }

    public LiveData<List<Task>> getListTaskLiveData(){
        return mListLiveData;
    }

    public void deleteTask(final Task task){
        AppExecutor.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                mAppDatabase.getTaskDao().delete(task);
            }
        });
    }

}
