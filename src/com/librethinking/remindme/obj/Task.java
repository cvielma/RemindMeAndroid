package com.librethinking.remindme.obj;

import java.io.Serializable;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable, Serializable{
    private String title;
    private String description;
    private TaskStatus status;
    private Date dueDate;
    private User owner;
    private User destinee;
    
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public Task(){}
    
    public Task(String title, String description, TaskStatus status,
        Date dueDate, User owner, User destinee) {
        super();
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
        this.owner = owner;
        this.destinee = destinee;
    }
    
    public Task(Parcel in){
        title = in.readString();
        description = in.readString();
        status = in.readParcelable(TaskStatus.class.getClassLoader());
        long date = in.readLong();
        dueDate = date==0? null : new Date(date);
        owner = in.readParcelable(User.class.getClassLoader());
        destinee = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeString(title);
        arg0.writeString(description);
        arg0.writeParcelable(status, arg1);
        arg0.writeLong(dueDate == null? 0:dueDate.getTime());
        arg0.writeParcelable(owner, arg1);
        arg0.writeParcelable(destinee, arg1);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public User getOwner() {
        return owner;
    }

    public User getDestinee() {
        return destinee;
    }

    @Override
    public String toString() {
        return getTitle();
    }
    
    

}
