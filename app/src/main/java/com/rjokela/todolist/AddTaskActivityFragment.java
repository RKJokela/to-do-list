package com.rjokela.todolist;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddTaskActivityFragment extends Fragment {
    public static final String TAG = "AddTaskActivityFragment";

    public static final String EXTRA_TASK = "task";

    public static final short REQUEST_CODE = (short) R.id.request_code_addtask;
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_CANCELED = 1;

    public AddTaskActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: add logic for activity
        if (true == new Random().nextBoolean()) {
            cancelButtonClick();
        } else {
            addButtonClick();
        }
    }

    public void cancelButtonClick() {
        getActivity().setResult(RESULT_CODE_CANCELED);
        getActivity().finishActivity(REQUEST_CODE);
    }

    public void addButtonClick() {
        Task task = new Task("dummy", "03/16/15");
        Intent i = new Intent();
        i.putExtra(EXTRA_TASK, task.toString());

        getActivity().setResult(RESULT_CODE_SUCCESS, i);
        getActivity().finishActivity(REQUEST_CODE);
    }
}
