package com.example.mvvmtodolist.detail;

import com.example.mvvmtodolist.model.Task;
import com.example.mvvmtodolist.model.TaskDao;

public class TaskViewModel {
    private Task addTask;
    private Task updateTask;
    private TaskDao taskDao;
    private String resultTask = "";
    private boolean setDeleteBtnVisibility = false;
    private boolean showTask = false;

    public TaskViewModel(TaskDao taskDao, Task updateTask) {

        this.taskDao = taskDao;
        addTask = new Task();
        this.updateTask = updateTask;
        if (updateTask != null) {
            setDeleteBtnVisibility = true;
            showTask = true;
        }
        else {

            setDeleteBtnVisibility = false;
            showTask =false;
        }
    }


    public void saveTask(int importance, String title) {
        if (title.isEmpty())
            return;
        if (updateTask == null) {
            addTask.setTitle(title);
            addTask.setPriority(importance);
            addTask.setChecked(false);
            long result = taskDao.insert(addTask);
            addTask.setId(result);
            resultTask = "add";
        } else {
            updateTask.setTitle(title);
            updateTask.setPriority(importance);
            int result = taskDao.update(updateTask);
            if (result > 0)
                resultTask = "update";
        }

    }

    public void deleteTask() {
        if (updateTask != null) {
            taskDao.delete(updateTask);
            resultTask = "delete";
        }
    }

    public String getResultTask() {
        return resultTask;
    }

    public Task getAddTask() {
        return addTask;
    }

    public Task getUpdateTask() {
        return updateTask;
    }

    public boolean isSetDeleteBtnVisibility() {
        return setDeleteBtnVisibility;
    }

    public boolean isShowTask() {
        return showTask;
    }
}
