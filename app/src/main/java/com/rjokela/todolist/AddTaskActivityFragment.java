package com.rjokela.todolist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;


/**
 * A placeholder fragment containing a simple view.
 */
public class AddTaskActivityFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static final String TAG = "AddTaskActivityFragment";

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESC = "desc";
    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_DETAILS = "details";

    public static final short REQUEST_CODE = (short) R.id.request_code_addtask;
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_CANCELED = 1;

    private EditText tvTitle;
    private EditText tvDesc;
    private EditText tvDate;
    private EditText tvDetails;
    private DateFormat df;

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

        // init text inputs
        tvTitle = (EditText) getActivity().findViewById(R.id.addTask_title);
        tvDesc = (EditText) getActivity().findViewById(R.id.addTask_desc);
        tvDate = (EditText) getActivity().findViewById(R.id.addTask_date);
        tvDetails = (EditText) getActivity().findViewById(R.id.addTask_details);

        // initialize the date to today for ease of use
        Date today = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        tvDate.setText(df.format(today));

        // instead of typing the date in the field, use a date picker dialog
        tvDate.setShowSoftInputOnFocus(false);
        // need to set both OnFocusChangeListener and OnClickListener for proper behavior
        tvDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String verb = hasFocus ? "received " : "lost ";
                Log.d(TAG, "Date view has " + verb + "focus");
                if (hasFocus) {
                    getDateFromPicker();
                }
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Date view was clicked");
                getDateFromPicker();
            }
        });

        // hook up buttons
        Button addButton = (Button) getActivity().findViewById(R.id.addTask_buttonAdd);
        Button cancelButton = (Button) getActivity().findViewById(R.id.addTask_buttonCancel);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButtonClick();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonClick();
            }
        });
    }

    public void cancelButtonClick() {
        Log.d(TAG, "cancelButtonClick: returning without saving");
        getActivity().setResult(RESULT_CODE_CANCELED);
        getActivity().finish();
    }

    public void addButtonClick() {
        String titleInput = tvTitle.getText().toString();
        String descInput = tvDesc.getText().toString();
        String dateInput = tvDate.getText().toString();
        String detailsInput = tvDetails.getText().toString();

        boolean validDate = checkDate(dateInput);

        if (!titleInput.isEmpty() && validDate) {
            Log.d(TAG, "addButtonClick: data is OK, returning new Task info");
            Intent i = new Intent();
            i.putExtra(EXTRA_TITLE, titleInput);
            i.putExtra(EXTRA_DESC, descInput);
            i.putExtra(EXTRA_DATE, dateInput);
            i.putExtra(EXTRA_DETAILS, detailsInput);

            getActivity().setResult(RESULT_CODE_SUCCESS, i);
            getActivity().finish();
        } else {
            if (titleInput.isEmpty())
                Log.d(TAG, "addButtonClick: Error - task title is blank");
            if (!validDate)
                Log.d(TAG, "addButtonClick: Error - date is not good (input: '" + dateInput + "')");
            Toast.makeText(getActivity(), R.string.add_task_error_toast, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkDate(String date) {
        try {
            Date d = df.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    // callback when date is picked: set the EditText to that date
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // make the date string based on what was picked
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tvDate.setText(df.format(cal.getTime()));
    }

    public void getDateFromPicker() {
        // scan the text currently in the field
        Scanner scanner = new Scanner(tvDate.getText().toString());
        scanner.useDelimiter("/");
        int month = scanner.nextInt() - 1;      // months start from zero
        int day = scanner.nextInt();
        int year = scanner.nextInt() + 2000;    // 4 digit year

        // make a date picker starting at that date
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.show();
    }
}
