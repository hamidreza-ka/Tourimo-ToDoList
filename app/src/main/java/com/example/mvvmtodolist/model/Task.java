package com.example.mvvmtodolist.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_tasks")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "is_completed")
    private Boolean isChecked;
    private String title;
    private int priority = IMPORTANCE_NORMAL;

    public static final int IMPORTANCE_HIGH = 2;
    public static final int IMPORTANCE_NORMAL = 1;
    public static final int IMPORTANCE_LOW = 0;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeValue(this.isChecked);
        dest.writeString(this.title);
        dest.writeInt(this.priority);
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.isChecked = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.title = in.readString();
        this.priority = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}


