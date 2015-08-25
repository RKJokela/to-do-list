package com.rjokela.todolist;

import android.util.Log;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Stores the data for a single item in a to-do list
 */
public class Task implements Comparable<Task> {
    public static final String TAG = "Task";

    // format of due date
    public static final String DATE_FORMAT_STRING = "MM/dd/yy";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.US);
    
    // sort options
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_ID = 2;
    public static final int SORT_BY_TITLE = 3;
    private static int sortMethod = SORT_BY_DATE;
    
    // fields
    private long id;
    private String title;
    private String description;
    private Date dueDate;
    private String details;
    
    // constructors
    public Task(String title, String dueDate) {
        id = 0;
        this.title = title;
        description = "";
        details = "";
        setDueDateString(dueDate);
    }
    public Task(long id, String title, String desc, String dueDate, String details) {
        this.id = id;
        this.title = title;
        this.description = desc;
        setDueDateString(dueDate);
        this.details = details;
    }
    
    // getters
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDueDate() { return dueDate; }
    public String getDueDateString() { return DATE_FORMAT.format(dueDate); }
    public String getDetails() { return details; }
    public int getSortMethod() { return sortMethod; }
    
    // setters
    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setDueDateString(String dueDate) {
        try {
            this.dueDate = DATE_FORMAT.parse(dueDate);
        } catch (Exception e) {
            Log.d(TAG, "setDueDateString - error parsing string '" + dueDate + "'", e);
        }
    }
    public void setDetails(String details) { this.details = details; }
    public static void setSortMethod(int sortBy) {
        if (sortBy >= SORT_BY_DATE && sortBy <= SORT_BY_TITLE)
            sortMethod = sortBy;
    }
    
    // toString
    @Override
    public String toString() { return title; }
    
    // Comparable interface
    public int compareTo(Task otherTask) {
        int retval = 0;
        switch (sortMethod) {
        case SORT_BY_DATE:
            retval = dueDate.compareTo(otherTask.getDueDate()); break;
        case SORT_BY_ID:
            retval = (Long.valueOf(id)).compareTo(otherTask.getId()); break;
        case SORT_BY_TITLE:
            retval = title.compareTo(otherTask.getTitle()); break;
        default:
            break;
        }        
        return retval;
    }
}
