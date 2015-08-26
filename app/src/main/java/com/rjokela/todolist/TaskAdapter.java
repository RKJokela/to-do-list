package com.rjokela.todolist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter to show Tasks in a ListView
 */
public class TaskAdapter extends ArrayAdapter<Task> {
	public static final String TAG = "TaskAdapter";
	
	private static final int LAYOUT = R.layout.to_do_list_item;
	private static final int TITLE  = R.id.to_do_list_item_title;
	private static final int DESC   = R.id.to_do_list_item_desc;
	private static final int DATE   = R.id.to_do_list_item_date;
	
	private LayoutInflater inflater;
	private List<Task> tasks;
	
	public TaskAdapter(Context context, List<Task> tasks) {
		super(context, LAYOUT, tasks); 
		this.inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tasks = tasks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get a holder for this position
		TaskHolder holder = null;
		if (convertView == null) {
			// new view, inflate from layout and set holder
			convertView = inflater.inflate(LAYOUT, parent, false);
			holder = new TaskHolder(convertView);
			convertView.setTag(holder);
		} else {
			// recycling view, just get holder
			holder = (TaskHolder) convertView.getTag();
		}
		
		// now set text from Task at this position
		try {
			Task task = tasks.get(position);
			holder.tvTitle.setText(task.getTitle());
			holder.tvDesc.setText(task.getDescription());
			holder.tvDate.setText(task.getDueDateString());
		} catch (Exception e) {
			Log.e(TAG,
				"getView failed. position: " + position
				+ ", list size: " + tasks.size(), e);
		}
	}
	
	// Holder to tag the views in their parent for recycling
	private static class TaskHolder {
		public TextView tvTitle;
		public TextView tvDesc;
		public TextView tvDate;
		public TaskHolder(View convertView) {
			tvTitle = (TextView) convertView.findViewById(TITLE);
			tvDesc  = (TextView) convertView.findViewById(DESC);
			tvDate  = (TextView) convertView.findViewById(DATE);
		}
	}
}
