package com.rjokela.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

public final class ToDoListDBHelper extends SQLiteOpenHelper {
    public static final String TAG = "ToDoListDBHelper";
    
    public ToDoListDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    public static final String DB_NAME = "todolist.db";
    public static final int DB_VERSION = 1;
    
    // define the tasks table
    public static abstract class TaskTable implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "desc";
        public static final String COLUMN_DUEDATE = "duedate";
        public static final String COLUMN_DETAILS = "details";
        public static final String COLUMN_COMPLETE = "complete";
        
        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE       + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DUEDATE     + " TEXT,"
                + COLUMN_DETAILS     + " TEXT,"
                + COLUMN_COMPLETE    + " BOOLEAN"
            + ")";
            
        public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    
    public void insert(Task task) {
        // get the database
        SQLiteDatabase db = getWritableDatabase();
        
        // insert the row
        long id = db.insert(TaskTable.TABLE_NAME, null, makeContentValues(task));
        task.setId(id);
    }
    
    public void insert(List<Task> newTasks) {
        for (Task t : newTasks) {
            insert(t);
        }
    }
    
    public boolean delete(Task task) {
        int deleted = getWritableDatabase().delete(TaskTable.TABLE_NAME,
            TaskTable._ID + "=" + task.getId(), null);
        return deleted > 0;
    }
    
    public int delete(List<Task> tasksToDelete) {
        int count = 0;
        for (Task t : tasksToDelete) {
            if (delete(t)) count++;
        }
        return count;
    }
    
    public void update(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        
        // select task by id and update the record
        db.update(TaskTable.TABLE_NAME,
            makeContentValues(task),
            TaskTable._ID + "=" + task.getId(),
            null);
    }
    
    public void update(List<Task> tasksToUpdate) {
        for (Task t : tasksToUpdate) {
            update(t);
        }
    }
    
    public void reset() {
        Log.d(TAG, "reset() called - clearing all entries");
        getWritableDatabase().delete(TaskTable.TABLE_NAME, null, null);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate() called");
        db.execSQL(TaskTable.SQL_CREATE_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        // not implemented!
        Log.e(TAG, "onUpgrade() not implemented");
    }
    
    private ContentValues makeContentValues(Task task) {
        // map the task to column names
        ContentValues values = new ContentValues();
        values.put(TaskTable.COLUMN_TITLE, task.getTitle());
        values.put(TaskTable.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TaskTable.COLUMN_DUEDATE, task.getDueDateString());
        values.put(TaskTable.COLUMN_DETAILS, task.getDetails());
        values.put(TaskTable.COLUMN_COMPLETE, task.isComplete());
        return values;
    }
}
