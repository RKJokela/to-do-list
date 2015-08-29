package com.rjokela.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;

import java.util.Comparator;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ToDoListActivityFragment extends Fragment {
    public static final String TAG = "ToDoListFragment";

    // request code for intent
    public static final short REQUEST_CODE = 42;
    // instance state keys
    public static final String SORT_METHOD = "sortmethod";

    private List<Task> tasks;
    private ArrayAdapter<Task> taskAdapter;
    private ToDoListDBHelper dbHelper;
    private int sortMethod;

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
        // on click, show details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(taskAdapter.getItem(position));
            }
        });
        // on long click, delete the task
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteTask(position);
                return true;
            }
        });
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored() called");

        // restore sorted state
        if (savedInstanceState != null) {
            sortMethod = savedInstanceState.getInt(SORT_METHOD, Task.SORT_BY_DATE);
            Log.d(TAG, "restoring from saved instance: sort method is " + sortMethod);
        }
        else {
            sortMethod = Task.SORT_BY_DATE;
            Log.d(TAG, "no saved instance state; using default sort by date");
        }
        sortTasks();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addtask:
                Intent i = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                return true;
            case R.id.action_sortDate:
                sortMethod = Task.SORT_BY_DATE;
                sortTasks();
                return true;
            case R.id.action_sortTitle:
                sortMethod = Task.SORT_BY_TITLE;
                sortTasks();
                return true;
            case R.id.action_sortCreated:
                sortMethod = Task.SORT_BY_ID;
                sortTasks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(SORT_METHOD, sortMethod);
        Log.d(TAG, "saving instance state, sort method is " + sortMethod);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case AddTaskActivityFragment.RESULT_CODE_SUCCESS:
                    String title = data.getStringExtra(AddTaskActivityFragment.EXTRA_TITLE);
                    String desc = data.getStringExtra(AddTaskActivityFragment.EXTRA_DESC);
                    String date = data.getStringExtra(AddTaskActivityFragment.EXTRA_DATE);
                    String details = data.getStringExtra(AddTaskActivityFragment.EXTRA_DETAILS);
                    addTask(new Task(0, title, desc, date, details, false));
                    break;
                case AddTaskActivityFragment.RESULT_CODE_CANCELED:
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        reloadFromDB();
        sortTasks();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    private void sortTasks() {
        String sortBy = "UNDEFINED";
        switch (sortMethod) {
            case Task.SORT_BY_DATE:
                sortBy = "date";
                break;
            case Task.SORT_BY_ID:
                sortBy = "id";
                break;
            case Task.SORT_BY_TITLE:
                sortBy = "title";
                break;
        }
        Log.d(TAG, "sorting tasks by " + sortBy);
        Task.setSortMethod(sortMethod);
        taskAdapter.sort(new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    private void reloadFromDB() {
        tasks = dbHelper.selectAll();
        taskAdapter.clear();
        taskAdapter.addAll(tasks);
    }

    private void deleteTask(int position) {
        Log.d(TAG, "deleting task at position " + position);
        Task toDelete = taskAdapter.getItem(position);
        Log.d(TAG, "Title: " + toDelete + ", ID: " + toDelete.getId());
        taskAdapter.remove(toDelete);
        taskAdapter.notifyDataSetChanged();

        dbHelper.delete(toDelete);
    }

    private void addTask(Task task) {
        Log.d(TAG, "adding task, Title: " + task);
        taskAdapter.add(task);
        taskAdapter.notifyDataSetChanged();

        dbHelper.insert(task);
        Log.d(TAG, "new task ID is " + task.getId());

        sortTasks();
    }

    private void showDetails(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String detailsLabel = getString(R.string.add_task_details_label);
        String details = task.getDetails();
        if (details.isEmpty())
            details = getString(R.string.no_details_message);
        builder
                .setTitle(task.getTitle() + " " + detailsLabel)
                .setMessage(details)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }
}
