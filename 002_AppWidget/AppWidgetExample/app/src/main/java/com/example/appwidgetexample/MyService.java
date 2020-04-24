package com.example.appwidgetexample;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyAppWidget.count++;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());

        int[] widgetsId = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), MyAppWidget.class));

        MyAppWidget.updateAppWidget(getApplicationContext(), appWidgetManager, widgetsId[0]);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
