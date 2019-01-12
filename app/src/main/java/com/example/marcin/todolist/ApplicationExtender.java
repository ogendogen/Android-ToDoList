package com.example.marcin.todolist;

import android.app.Application;

public class ApplicationExtender extends Application
{
    public static TaskList tasks = new TaskList();
}
