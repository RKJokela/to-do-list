package com.rjokela.todolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 * A placeholder fragment containing a simple view.
 */
public class ToDoListActivityFragment extends Fragment {
    public static final String TAG = "ToDoListActivityFragment";

    private Task[] tasks;
    private ListAdapter taskAdapter;

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

        // TODO: replace this placeholder task array
        tasks = new Task[] {
                new Task("Do laundry", "03/16/15"),
                new Task("Wash dishes", "08/02/15"),
                new Task("Make bed", "09/22/15")
        };

        taskAdapter = new ArrayAdapter<Task>(getActivity(), android.R.layout.simple_list_item_1, tasks);

        ListView listView = (ListView) getActivity().findViewById(R.id.taskList_listView);
        listView.setAdapter(taskAdapter);
    }
}
