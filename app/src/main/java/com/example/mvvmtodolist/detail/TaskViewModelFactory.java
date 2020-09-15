package com.example.mvvmtodolist.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmtodolist.model.Task;
import com.example.mvvmtodolist.model.TaskDao;

public class TaskViewModelFactory implements ViewModelProvider.Factory {

    private final TaskDao taskDao;
    private final Task updateTask;

    public TaskViewModelFactory(TaskDao taskDao, Task updateTask) {
        this.taskDao = taskDao;
        this.updateTask = updateTask;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskViewModel(taskDao,updateTask);
    }
}
