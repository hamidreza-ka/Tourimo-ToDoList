package com.example.mvvmtodolist.detail;

import androidx.lifecycle.ViewModel;

import com.example.mvvmtodolist.model.Task;
import com.example.mvvmtodolist.model.TaskDao;

public class TaskViewModel extends ViewModel {
  //  private Task addTask;
    private Task updateTask;
    private TaskDao taskDao;
    private boolean setDeleteBtnVisibility;
    private boolean showTask;

    public TaskViewModel(TaskDao taskDao, Task updateTask) {

        this.taskDao = taskDao;

        this.updateTask = updateTask;
        if (updateTask != null) {
            setDeleteBtnVisibility = true;
            showTask = true;
        } else {
            setDeleteBtnVisibility = false;
            showTask = false;
        }
    }


    public void saveTask(int importance, String title) {
        if (title.isEmpty())
            return;
        if (updateTask == null) {
            updateTask = new Task();
            updateTask.setTitle(title);
            updateTask.setPriority(importance);
            updateTask.setChecked(false);
            long result = taskDao.insert(updateTask);
            updateTask.setId(result);
        } else {
            updateTask.setTitle(title);
            updateTask.setPriority(importance);
            taskDao.update(updateTask);

        }


    }

    public void deleteTask() {
        if (updateTask != null) {
            taskDao.delete(updateTask);
        }
    }

    public boolean isSetDeleteBtnVisibility() {
        return setDeleteBtnVisibility;
    }

    public boolean isShowTask() {
        return showTask;
    }
}
