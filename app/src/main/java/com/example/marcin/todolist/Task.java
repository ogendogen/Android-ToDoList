package com.example.marcin.todolist;

import android.location.Address;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements Cloneable
{
    private String name;
    private String description;
    private int priority;
    private Date dueDate;
    private Boolean isExpired;

    public Task(String name, String description, int priority, Date dueDate)
    {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isExpired = false;
    }

    public Task(Task task)
    {
        //this(task.getName(), task.getDescription(), task.getPriority(), task.getDueDate());
        this.name = task.getName();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.dueDate = task.getDueDate();
        this.isExpired = task.getIsExpired();
    }

    @Override
    public Object clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Task(this);
        }
    }

    public static void validateData(String taskName, String taskDesc, int priority, Date dueDate, Boolean fromFile) throws Exception
    {
        if (priority < 0 || priority > 5) throw new Exception("Niepoprawny priorytet!");
        if (taskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
        if (taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
        if (dueDate.before(new Date()) && !fromFile) throw new Exception("Wybrany czas już minął!");
    }

    public int getHashCode()
    {
        return (name.hashCode() * description.hashCode() * dueDate.hashCode()) / (priority == 0 ? 6 : priority);
    }

    public static int getHashCodeFromValues(String name, String description, String s_priority, String s_dueDate) throws NumberFormatException, ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        int priority = Integer.parseInt(s_priority);
        Date dueDate = format.parse(s_dueDate);

        return (name.hashCode() * description.hashCode() * dueDate.hashCode()) / (priority == 0 ? 6 : priority);
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

    public Boolean getIsExpired() { return isExpired; }
}
