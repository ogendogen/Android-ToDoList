package com.example.marcin.todolist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;

public class TaskList
{
    private ArrayList<Task> tasks;

    public enum SortCriteria
    {
        DueDate,
        Priority,
        TaskNameAsc,
        TaskNameDesc
    }

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

    public void addTask(String taskName, String taskDesc, int priority, Date dueDate, Boolean fromFile) throws Exception
    {
        if (priority < 0 || priority > 5) throw new Exception("Niepoprawny priorytet!");
        if (taskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
        if (taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
        if (dueDate.before(new Date()) && !fromFile) throw new Exception("Wybrany czas już minął!");

        tasks.add(new Task(taskName, taskDesc, priority, dueDate));
    }

    public int length()
    {
        return tasks.size();
    }

    public Task get(int index) throws ArrayIndexOutOfBoundsException
    {
        try
        {
            return tasks.get(index);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        return null;
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

    public void sortTasks(SortCriteria sortCriterium) throws Exception
    {
        try
        {
            if (this.tasks.size() == 0) throw new Exception("Lista zadań jest pusta!");
            if (this.tasks.size() == 1) throw new Exception("Masz tylko jedno zadanie!");

            ArrayList<Task> sortedTasks = new ArrayList<Task>(tasks.size());
            switch(sortCriterium)
            {
                case DueDate:
                {
                    while (!tasks.isEmpty())
                    {
                        Date earliestDate = tasks.get(0).getDueDate();
                        Task earliestTask = tasks.get(0);
                        int indexToDelete = 0;
                        for (int i=0; i<tasks.size(); i++)
                        {
                            Date currentDate = tasks.get(i).getDueDate();
                            if (currentDate.before(earliestDate))
                            {
                                earliestDate = currentDate;
                                earliestTask = new Task(tasks.get(i));
                                indexToDelete = i;
                            }
                        }

                        sortedTasks.add(new Task(earliestTask));
                        tasks.remove(indexToDelete);
                    }
                    break;
                }
                case Priority:
                {
                    while (!tasks.isEmpty())
                    {
                        int greatestPriority = tasks.get(0).getPriority();
                        Task earliestTask = tasks.get(0);
                        int indexToDelete = 0;
                        for (int i=0; i<tasks.size(); i++)
                        {
                            int currentPriority = tasks.get(i).getPriority();
                            if (currentPriority > greatestPriority)
                            {
                                greatestPriority = currentPriority;
                                earliestTask = new Task(tasks.get(i));
                                indexToDelete = i;
                            }
                        }
                        sortedTasks.add(new Task(earliestTask));
                        tasks.remove(indexToDelete);
                    }
                    break;
                }
                case TaskNameAsc:
                {
                    ArrayList<String> taskNames = new ArrayList<String>(tasks.size());
                    for (Task task : tasks) taskNames.add(task.getName());
                    Collections.sort(taskNames);
                    for (String taskName : taskNames)
                    {
                        Task task = getTaskByName(taskName);
                        sortedTasks.add(new Task(task));
                    }
                    break;
                }
                case TaskNameDesc:
                {
                    ArrayList<String> taskNames = new ArrayList<String>(tasks.size());
                    for (Task task : tasks) taskNames.add(task.getName());
                    Collections.sort(taskNames);
                    Collections.reverse(taskNames);
                    for (String taskName : taskNames)
                    {
                        Task task = getTaskByName(taskName);
                        sortedTasks.add(new Task(task));
                    }
                    break;
                }
            }
            this.tasks = sortedTasks; // ??? clone() ?
            int a = 1;
        }
        catch(Exception e)
        {
            throw e;
        }
    }

    private Task getTaskByName(String name)
    {
        for (Task task : tasks)
        {
            if (task.getName().equals(name)) return task;
        }
        return null;
    }
}
