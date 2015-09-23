package com.librethinking.remindme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.librethinking.remindme.obj.Task;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
    int resource;
    FragmentActivity context;
    
    public TaskArrayAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
        this.resource = resource;
        //TODO: I don't think this is very good
        this.context = (FragmentActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RelativeLayout todoView;

        Task item = getItem(position);

        String taskTitle = item.getTitle();
        Date dueDate = item.getDueDate();
        String owner = item.getOwner().getFirstName() + "" +item.getOwner().getLastName();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String dateString = dueDate == null? "-" : sdf.format(dueDate);

        if (convertView == null) {
          todoView = new RelativeLayout(getContext());
          String inflater = Context.LAYOUT_INFLATER_SERVICE;
          LayoutInflater li;
          li = (LayoutInflater)getContext().getSystemService(inflater);
          li.inflate(resource, todoView, true);
        } else {
          todoView = (RelativeLayout) convertView;
        }

        todoView.setClickable(true);
        todoView.setOnLongClickListener(new OnLongClickListener() {
            
            @Override
            public boolean onLongClick(View v) {
                DialogFragment dialog = new TodoListDialog();
                //TODO: should be done using setAttributes
                ((TodoListDialog)dialog).setTaskId(position);
                
                dialog.show(TaskArrayAdapter.this.context.getSupportFragmentManager(), "tododialog");
                return true;
            }
        });

        TextView dueDateView = (TextView)todoView.findViewById(R.id.taskDueDate);
        TextView titleView = (TextView)todoView.findViewById(R.id.taskTitle);
        TextView ownerView = (TextView)todoView.findViewById(R.id.taskOwner);

        dueDateView.setText(dateString);
        titleView.setText(taskTitle);
        ownerView.setText(owner);

        return todoView;
    }
    
}
