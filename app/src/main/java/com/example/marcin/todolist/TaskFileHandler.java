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
            if (stream != null) stream.close();
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
        finally
        {
            if (in != null) in.close();
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
            if (in != null) in.close();
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

    public static void editTask(Context context, int index, String taskName, String taskDesc, int priority, Date dueDate)
    {
        FileOutputStream stream = null;
        try
        {
            String rawFile = readRaw(context);

            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");
            file.delete();
            file.createNewFile();

            String[] tasks = rawFile.split("\n");
            StringBuilder finalOutput = new StringBuilder();
            for (int i=0; i<tasks.length; i++)
            {
                if (i == index)
                {
                    SimpleDateFormat format = new SimpleDateFormat("d-M-Y H:m", Locale.ENGLISH);
                    finalOutput.append(taskName + "|" + taskDesc + "|" + String.valueOf(priority) + "|" + format.format(dueDate) + "\n");
                }
                else
                {
                    finalOutput.append(tasks[i] + "\n");
                }
            }

            stream = new FileOutputStream(file, false);
            stream.write(finalOutput.toString().getBytes());
            stream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Błąd edycji: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void removeTask(Context context, int index) throws Exception
    {
        FileOutputStream stream = null;
        try
        {
            String rawFile = readRaw(context);

            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");

            String[] tasks = rawFile.split("\n");
            StringBuilder builder = new StringBuilder();
            for (int i=0; i<tasks.length; i++)
            {
                if (i == index) continue;
                String[] parts = tasks[i].split("\\|");
                builder.append(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "\n");
            }
            file.delete();
            file.createNewFile();

            stream = new FileOutputStream(file, false);
            stream.write(builder.toString().getBytes());
            stream.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Błąd usuwania: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }
        finally
        {
            if (stream != null) stream.close();
        }
    }

    public static String[] getTaskPartsById(Context context, int index)
    {
        BufferedReader in = null;
        if (index == -1) return null;

        try
        {
            File path = context.getFilesDir();
            File file = new File(path, "tasks.txt");

            StringBuilder builder = new StringBuilder();
            String line;

            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), "tasks.txt")));
            while ((line = in.readLine()) != null) builder.append(line + "\n");

            String buffer = builder.toString();
            int counter = 0;

            String[] lines = buffer.split("\n");
            return lines[index].split("\\|");
        }
        catch(IndexOutOfBoundsException e)
        {
            e.printStackTrace();
            Toast.makeText(context, "Edytowane zadanie nie istnieje!", Toast.LENGTH_LONG).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, "getTaskPartsById: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        return null;
    }
}
