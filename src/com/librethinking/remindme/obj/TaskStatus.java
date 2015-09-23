package com.librethinking.remindme.obj;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Parcelable enum.
 * 
 * @author cvielma
 *
 */
public class TaskStatus implements Parcelable, Serializable {
     
    public final static TaskStatus ACTIVE = new TaskStatus(1); 
    public final static TaskStatus DONE = new TaskStatus(2);
    public final static TaskStatus DUE = new TaskStatus(3);
    private int status;
    
    public static final Parcelable.Creator<TaskStatus> CREATOR = new Parcelable.Creator<TaskStatus>() {
        public TaskStatus createFromParcel(Parcel in) {
            return new TaskStatus(in);
        }

        public TaskStatus[] newArray(int size) {
            return new TaskStatus[size];
        }
    };
    
    private TaskStatus(int status) {
        super();
        this.status = status;
    }
    
    public TaskStatus(Parcel in) {
        super();
        this.status = in.readInt();
    }

    public int getStatus() {
        return status;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + status;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskStatus other = (TaskStatus) obj;
        if (status != other.status)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(status);
    }

}
