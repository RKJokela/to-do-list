package com.rjokela.todolist;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class AddTaskActivity extends ActionBarActivity {

    @Override
    public void onBackPressed() {
        Log.d(AddTaskActivityFragment.TAG, "onBackPressed() called, overridden to mimic cancel button");
        AddTaskActivityFragment fragment =
                (AddTaskActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_add_task);
        fragment.cancelButtonClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
    }
}
