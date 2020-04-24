package com.example.todolist;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutor {

    private static final Object LOCK = new Object();

    private static AppExecutor mAppExecutor;

    private Executor mDiskIo;

    private AppExecutor(Executor diskIo){
        mDiskIo = diskIo;
    }

    public static AppExecutor getInstance(){
        synchronized (LOCK){
            if(mAppExecutor == null){
                mAppExecutor = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return mAppExecutor;
    }

    public Executor getDiskIo(){
        return mDiskIo;
    }
}
