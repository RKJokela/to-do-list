package com.rjokela.todolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ToDoListActivityFragment extends Fragment {
    public static final String TAG = "ToDoListFragment";

    private List<Task> tasks;
    private ArrayAdapter<Task> taskAdapter;
    private ToDoListDBHelper dbHelper;

    public ToDoListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // init data
        dbHelper = new ToDoListDBHelper(getActivity());
        tasks = dbHelper.selectAll();
        taskAdapter = new TaskAdapter(getActivity(), tasks);

        // init actionbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.taskList_toolBar);
        ((ActionBarActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // init list view
        ListView listView = (ListView) getActivity().findViewById(R.id.taskList_listView);
        listView.setAdapter(taskAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTask(position);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_addtask) {
            // TODO: remove this line
            addTask(new Task("Finish Homework", "08/31/15"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void deleteTask(int position) {
        Log.d(TAG, "deleting task at position " + position);
        Task toDelete = taskAdapter.getItem(position);
        Log.d(TAG, "Title: " + toDelete + ", ID: " + toDelete.getId());
        taskAdapter.remove(toDelete);
        taskAdapter.notifyDataSetChanged();

        dbHelper.delete(toDelete);
    }

    void addTask(Task task) {
        Log.d(TAG, "adding task, Title: " + task);
        taskAdapter.add(task);
        taskAdapter.notifyDataSetChanged();

        dbHelper.insert(task);
        Log.d(TAG, "new task ID is " + task.getId());
    }
}
