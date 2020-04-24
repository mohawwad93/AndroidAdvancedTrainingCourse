package com.example.todolist.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatDate(long millisecond) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());
        return sdfDate.format(new Date(millisecond));
    }
}
