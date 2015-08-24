package com.rjokela.todolist;

// TODO: add imports as needed
import java.util.Date;

/**
 * Stores the data for a single item in a to-do list
 */
public class Task implements Comparable<Task> {
    // format of due date
    public static final String DATE_FORMAT = "MM/dd/yy";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    
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
        description = new String("");
        details = new String("");
        this.dueDate = dateFormat.parse(dueDate);
    }
    public Task(long id, String title, String desc, String dueDate, String details) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.dueDate = dateFormat.parse(dueDate);
        this.details = details;
    }
    
    // getters
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDueDate() { return dueDate; }
    public String getDueDateString() { return dateFormat.format(dueDate); }
    public String getDetails() { return details; }
    public int getSortMethod() { return sortMethod; }
    
    // setters
    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setDueDateString(String dueDate) { this.dueDate = dateFormat.parse(dueDate); }
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
            retval = Integer.compare(id, otherTask.getId()); break;
        case SORT_BY_TITLE:
            retval = title.compareTo(otherTask.getTitle()); break;
        default:
            break;
        }        
        return retval;
    }
}
