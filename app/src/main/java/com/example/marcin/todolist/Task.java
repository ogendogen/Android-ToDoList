package com.example.marcin.todolist;

import java.util.Date;

public class Task
{
    private String name;
    private String description;
    private int priority;
    private Date dueDate;
    private Boolean isExpired;

    public Task(String name, String description, int priority, Date dueDate) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isExpired = false;
    }

    public static void validateData(String taskName, String taskDesc, int priority, Date dueDate, Boolean fromFile) throws Exception
    {
        if (priority < 0 || priority > 5) throw new Exception("Niepoprawny priorytet!");
        if (taskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
        if (taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
        if (dueDate.before(new Date()) && !fromFile) throw new Exception("Wybrany czas już minął!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
