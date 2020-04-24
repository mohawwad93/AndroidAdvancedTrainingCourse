package com.example.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddTaskVMFactory extends ViewModelProvider.NewInstanceFactory {

    private Application mApplication;
    private int mTaskId;

    public AddTaskVMFactory(Application application, int taskId){
        mApplication = application;
        mTaskId = taskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTaskViewModel(mApplication, mTaskId);
    }
}
