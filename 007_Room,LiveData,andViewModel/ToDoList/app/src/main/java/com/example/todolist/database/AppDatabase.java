package com.example.todolist.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "TODO";
    private static final Object LOCK = new Object();
    private static AppDatabase mAppDatabase;

    public static AppDatabase getInstance(Application application){
        synchronized (LOCK){
            if(mAppDatabase == null){
                mAppDatabase = Room.databaseBuilder(application, AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return mAppDatabase;
    }

    public abstract TaskDao getTaskDao();
}
