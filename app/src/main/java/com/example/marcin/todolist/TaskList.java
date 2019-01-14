package com.example.marcin.todolist;

import java.util.ArrayList;
import java.util.Date;

public class TaskList
{
    private ArrayList<Task> tasks;
    public TaskList()
    {
        tasks = new ArrayList<Task>();
    }

    public ArrayList<Task> getTasks()
    {
        return tasks;
    }

    public void overwriteFile() throws Exception
    {
        throw new Exception("Not implemented yet!");
    }

    public void addTask(String taskName, String taskDesc, int priority, Date dueDate) throws Exception
    {
        if (priority < 0 || priority > 5) throw new Exception("Niepoprawny priorytet!");
        if (taskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
        if (taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
        if (dueDate.before(new Date())) throw new Exception("Wybrany czas już minął!");

        Task task = new Task(taskName, taskDesc, priority, dueDate);
        tasks.add(task);
    }

    public int length()
    {
        return tasks.size();
    }

    public void removeTask(int index) throws Exception
    {
        try
        {
            tasks.remove(index);
        }
        catch(Exception e)
        {
            throw e;
        }
    }

    public void editTask(int index, String taskName, String taskDesc, int priority, Date dueDate) throws Exception
    {
        Task task = (Task)tasks.get(index);
        try
        {
            if (taskName != null && !taskName.isEmpty())
            {
                if (taskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
                else task.setName(taskName);
            }
            if (taskDesc != null && !taskDesc.isEmpty())
            {
                if (taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
                else task.setDescription(taskDesc);
            }
            if (priority != -1)
            {
                if (priority < 0 || priority > 5) throw new Exception("Niepoprawny priorytet!");
                else task.setPriority(priority);
            }
            if (dueDate != null)
            {
                if (dueDate.before(new Date())) throw new Exception("Wybrany czas już minął!");
                else task.setDueDate(dueDate);
            }
        }
        catch(Exception e)
        {
            throw e;
        }
    }
}
