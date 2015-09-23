package com.librethinking.remindme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.librethinking.remindme.obj.Task;
import com.librethinking.remindme.obj.User;

public class TodoListMainActivity extends FragmentActivity implements
        TodoListDialog.TodoListAlertDialogListener {
    private static final int ADD_TASK = 1;
    public static final String NEW_TASK_ADDED = "NEW_TASK_ADDED";
    public static final String OWNER_INFO = "OWNER_INFO";
    private static final String SAVEFILE = "TodoListMainActivity.data";
    private static final String LOG_TAG = "REMINDME-LOG";
    private User owner;

    private List<Task> tasksList;
    private TaskArrayAdapter arrayAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        getOwner(); // Automatically loads the variable

        // Get references to the Fragments
        FragmentManager fm = getSupportFragmentManager();
        ToDoListFragment todoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.TodoListFragment);

        // Create the array list of to do items
        tasksList = restoreTasksList();

        // Create the array adapter to bind the array to the ListView
        int resID = R.layout.listview_todo_item;
        arrayAdap = new TaskArrayAdapter(this, resID, tasksList);

        // Bind the array adapter to the ListView.
        todoListFragment.setListAdapter(arrayAdap);
    }

    private List<Task> restoreTasksList() {
        List<Task> result = new ArrayList<Task>();
        try {
            FileInputStream fis;
            fis = openFileInput(TodoListMainActivity.SAVEFILE);
            ObjectInputStream is = new ObjectInputStream(fis);
            Object temp = is.readObject();
            result = (List<Task>) temp;
            Log.d(LOG_TAG,
                    "Reading Object is: "
                            + Arrays.deepToString(result.toArray()));
            is.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public User getOwner() {
        // TODO: get real owner info
        // TODO: synchronize
        if (this.owner == null) {
            this.owner = new User();
            this.owner
                    .setFirstName(getString(R.string.default_owner_firstname));
            this.owner.setLastName("");
            this.owner.setEmail("");
        }
        return this.owner;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_list, menu);
        // ActionBar actionBar = getActionBar();
        // actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.add_task:
            return createTaskView();
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    // Opens a new task activity
    private boolean createTaskView() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(OWNER_INFO, (Parcelable) owner);
        startActivityForResult(intent, ADD_TASK);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TASK) {
            if (resultCode == RESULT_OK) {
                Task task = (Task) data.getParcelableExtra(NEW_TASK_ADDED);
                addTask(task);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addTask(Task task) {
        tasksList.add(0, task);
        arrayAdap.notifyDataSetChanged();
        Toast toast = Toast.makeText(this, R.string.task_created,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    // TODO: how to save only once and not each time the activity is not shown?
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            FileOutputStream fos;
            fos = openFileOutput(TodoListMainActivity.SAVEFILE,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(tasksList);
            Log.d(LOG_TAG,
                    "Saving Object is: "
                            + Arrays.deepToString(tasksList.toArray()));
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void editTask(DialogFragment dialog, long taskId) {
        Toast toast = Toast.makeText(this, "Edit Task "+tasksList.get((int) taskId).getTitle(),
                Toast.LENGTH_SHORT);
        toast.show();
        dialog.dismiss();
    }

    @Override
    public void markTaskDone(DialogFragment dialog, long taskId) {
        Task markDone = tasksList.get((int) taskId);
        Toast toast = Toast.makeText(this, "Marked as done "+tasksList.get((int) taskId).getTitle(),
                Toast.LENGTH_SHORT);
        toast.show();
        arrayAdap.notifyDataSetChanged();
        dialog.dismiss();
    }

    @Override
    public void deleteTask(DialogFragment dialog, long taskId) {
        Task delete = tasksList.get((int) taskId);
        tasksList.remove(delete);
        Toast toast = Toast.makeText(this,"Deleted "+delete.getTitle(),
                Toast.LENGTH_SHORT);
        toast.show();
        arrayAdap.notifyDataSetChanged();
        dialog.dismiss();
     }

}
