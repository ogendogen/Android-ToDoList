package com.example.marcin.todolist;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import static android.content.ContentValues.TAG;

public class FileExporter extends Activity
{
    private File file;

    public FileExporter(File path)
    {
        this.file = path;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public  boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public Boolean exportFile(String rawContent) throws Exception
    {
        if (!isExternalStorageWritable()) return false;

        int taskCounter = 0;
        FileOutputStream stream = null;
        try
        {
            if (file.exists())
            {
                file.delete();
                file.createNewFile();
            }

            stream = new FileOutputStream(file);
            String[] lines = rawContent.split("\n");
            StringBuilder builder = new StringBuilder();
            for (String line : lines)
            {
                taskCounter++;
                String[] parts = line.split("\\|");
                builder.append("Zadanie nr " + String.valueOf(taskCounter) + "\n");
                builder.append("Nazwa: " + parts[0] + "\n");
                builder.append("Opis: " + parts[1] + "\n");
                builder.append("Priorytet: " + parts[2] + "\n");
                builder.append("Data wykonania: " + parts[3] + "\n\n");
            }
            stream.write(builder.toString().getBytes());
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if (stream != null) stream.close();
        }

        return true;
    }
}
