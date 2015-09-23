package com.librethinking.remindme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.librethinking.remindme.obj.Task;
import com.librethinking.remindme.obj.TaskStatus;
import com.librethinking.remindme.obj.User;

public class AddTaskActivity extends Activity {
    private Calendar dueDate = null;
    private Calendar dueTime = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        
        /** some portions based on https://plus.google.com/+RomanNurik/posts/R49wVvcDoEW*/
     // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task task = AddTaskActivity.this.createTask();
                        if(task!=null){
                            Intent result = new Intent((String) null);
                            result.putExtra(TodoListMainActivity.NEW_TASK_ADDED, (Parcelable) task);
                            setResult(RESULT_OK, result);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }
                });
        
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        
        //Listeners for date and time pickers
        final TextView date = (TextView) findViewById(R.id.datepicker);
        date.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddTaskActivity.this.showDateDialog();
                    }
                });
        final TextView time = (TextView) findViewById(R.id.timepicker);
        time.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddTaskActivity.this.showTimeDialog();
                    }
                });
        

    }

    protected void showTimeDialog() {
        GregorianCalendar now = new GregorianCalendar();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, 
                new OnTimeSetListener(){
            
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                            int minute){
                        GregorianCalendar now = new GregorianCalendar();
                        GregorianCalendar dueDate = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                        AddTaskActivity.this.setTime(dueDate);
                        
                        if(AddTaskActivity.this.dueDate==null){
                            AddTaskActivity.this.setDate(dueDate);
                        }
                    }
                }
        ,now.get(Calendar.HOUR_OF_DAY) , now.get(Calendar.MINUTE) ,false);

        timePickerDialog.show();
    }

    protected void showDateDialog() {
        GregorianCalendar now = new GregorianCalendar();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, 
                new OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year,
                            int monthOfYear, int dayOfMonth) {
                        GregorianCalendar dueDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0,0);
                        
                        AddTaskActivity.this.setDate(dueDate);
                        
                        if(AddTaskActivity.this.dueTime==null){
                            AddTaskActivity.this.setTime(dueDate);
                        }
                    }
                }
        , now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    protected void setTime(GregorianCalendar dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        String dateString = sdf.format(dueDate.getTime());
        TextView time = (TextView) AddTaskActivity.this.findViewById(R.id.timepicker);
        time.setText(dateString);
        AddTaskActivity.this.dueTime = dueDate;
    }
    
    protected void setDate(GregorianCalendar dueDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.US);
        String dateString = sdf.format(dueDate.getTime());
        TextView date = (TextView) AddTaskActivity.this.findViewById(R.id.datepicker);
        date.setText(dateString);
        AddTaskActivity.this.dueDate = dueDate;
    }
    
    

    protected Task createTask() {
        final TextView titleView = (TextView) findViewById(R.id.title);
        String title = titleView.getText().toString();
        if(title!=null && !title.equals("")){
            String description = ((TextView) findViewById(R.id.description)).getText().toString();
            Bundle b = this.getIntent().getExtras();
            User owner = (User) this.getIntent().getParcelableExtra(TodoListMainActivity.OWNER_INFO);
            Date taskDueDate = null;
            if(this.dueDate !=null && this.dueTime !=null){
                taskDueDate = (new GregorianCalendar(
                                        this.dueDate.get(Calendar.YEAR), 
                                        this.dueDate.get(Calendar.MONTH), 
                                        this.dueDate.get(Calendar.DAY_OF_MONTH), 
                                        this.dueTime.get(Calendar.HOUR_OF_DAY), 
                                        this.dueTime.get(Calendar.MINUTE))
                            ).getTime();
            }
            Task task = new Task(title, description,TaskStatus.ACTIVE, taskDueDate, owner, new User());
            return task;
        }
        return null;
    }

}
