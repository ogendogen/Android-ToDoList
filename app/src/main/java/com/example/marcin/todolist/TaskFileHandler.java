package com.example.marcin.todolist;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TaskFileHandler
{
    public static void writeTask(Context context, Task task) throws Exception
    {
        FileOutputStream stream = null;
        try
        {
            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");
            if (file.exists()) stream = new FileOutputStream(file, true); else stream = new FileOutputStream(file, false);

            String taskName = task.getName();
            String taskDesc = task.getDescription();
            String priority = String.valueOf(task.getPriority());

            SimpleDateFormat format = new SimpleDateFormat("d-M-Y H:m", Locale.ENGLISH);
            String dueDate = format.format(task.getDueDate()); //format.pa//String.valueOf(task.getDueDate());

            String finalLine = "";
            String rawFile = TaskFileHandler.readRaw(context);
            //finalLine = (!rawFile.isEmpty() ? rawFile + "\n" : "") + taskName + "|" + taskDesc + "|" + priority + "|" + dueDate + "\n";
            finalLine = taskName + "|" + taskDesc + "|" + priority + "|" + dueDate + "\n";

            stream.write(finalLine.getBytes());
        }
        catch(Exception e)
        {
            Toast.makeText(context, "writeTask: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                stream.close();
            }
            catch(NullPointerException e)
            {
                Toast.makeText(context, "writeTask2: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String readRaw(Context context) throws Exception
    {
        BufferedReader in = null;
        try
        {
            StringBuilder builder = new StringBuilder();
            String line;

            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), "tasks.txt")));
            while ((line = in.readLine()) != null) builder.append(line);

            return builder.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static TaskList readAllTasks(Context context) throws Exception
    {
        BufferedReader in = null;
        try
        {
            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");

            StringBuilder builder = new StringBuilder();
            String line;

            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), "tasks.txt")));
            while ((line = in.readLine()) != null) builder.append(line + "\n");

            String buffer = builder.toString();
            String[] lines = buffer.split("\n");
            TaskList taskList = new TaskList();
            for (int i=0; i<lines.length; i++)
            {
                String[] parts = lines[i].split("\\|");

                if (parts.length != 4)
                    throw new Exception("Brakujące elementy w pliku! Len = " + parts.length);
                SimpleDateFormat format = new SimpleDateFormat("d-M-Y H:m", Locale.ENGLISH);

                Log.d("date", format.parse(parts[3]).toString());

                taskList.addTask(parts[0], parts[1], Integer.parseInt(parts[2]), format.parse(parts[3]), true);
            }

            return taskList;
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            Toast.makeText(context, "readAllTasks: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally
        {
            try
            {
                if (in != null) in.close();
            }
            catch(NullPointerException e)
            {
                Toast.makeText(context, "readAllTasks2: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        return null;
    }

    public static void deleteFile(Context context)
    {
        try
        {
            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");
            file.delete();
        }
        catch(Exception e)
        {
            Toast.makeText(context, "deleteFile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
