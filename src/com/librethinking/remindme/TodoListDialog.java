package com.librethinking.remindme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class TodoListDialog extends DialogFragment {
    private long taskId;
    private TodoListAlertDialogListener mListener;
    
    public interface TodoListAlertDialogListener {
        public void editTask(DialogFragment dialog, long taskId);
        public void markTaskDone(DialogFragment dialog, long taskId);
        public void deleteTask(DialogFragment dialog, long taskId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_action);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.todo_list_dialog, null);
        
        dialogView.findViewById(R.id.edit_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListDialog.this.mListener.editTask(TodoListDialog.this, TodoListDialog.this.taskId);
            }
        });
        
        dialogView.findViewById(R.id.mark_as_done_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListDialog.this.mListener.markTaskDone(TodoListDialog.this, TodoListDialog.this.taskId);
            }
        });
        
        dialogView.findViewById(R.id.delete_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListDialog.this.mListener.deleteTask(TodoListDialog.this, TodoListDialog.this.taskId);
            }
        });
        
        builder.setView(dialogView);
        
        return builder.create();
    }
    
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (TodoListAlertDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement TodoListAlertDialogListener");
        }
    }

}
