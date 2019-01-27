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

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
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
            while ((line = in.readLine()) != null) builder.append(line + "\n");

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
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);

                Log.d("dateRaw", parts[3]);
                Log.d("dateParsed", format.parse(parts[3]).toString());

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
                String[] parts = tasks[i].split("\\|");
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                int oldHashCode = Task.getHashCodeFromValues(parts[0], parts[1], format.parse(parts[3]), Integer.parseInt(parts[2]));

                if (oldHashCode == index)
                {
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
            Toast.makeText(context, "Błąd edycji: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                String[] parts = tasks[i].split("\\|");

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                if (index == Task.getHashCodeFromValues(parts[0], parts[1], format.parse(parts[3]), Integer.parseInt(parts[2]))) continue;

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

    public static void reWriteToFile(Context context, TaskList tasks)
    {
        try
        {

        }
        catch(Exception e)
        {
            Toast.makeText(context, "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static String[] getTaskPartsById(Context context, int hashCode)
    {
        BufferedReader in = null;
        if (hashCode == -1) return null;

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
            for (String taskline : lines)
            {
                String[] parts = taskline.split("\\|");
                String name = parts[0];
                String description = parts[1];
                String s_date = parts[3];

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                Date dueDate = format.parse(s_date);

                int priority = Integer.parseInt(parts[2]);
                int task_hashCode = Task.getHashCodeFromValues(name, description, dueDate, priority);
                if (task_hashCode == hashCode) return parts;
            }

            return null;
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
