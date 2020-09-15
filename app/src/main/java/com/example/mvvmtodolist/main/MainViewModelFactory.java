package com.example.mvvmtodolist.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmtodolist.model.AppDatabase;
import com.example.mvvmtodolist.model.TaskDao;

public class MainViewModelFactory implements ViewModelProvider.Factory {


    private TaskDao taskDao;

    public MainViewModelFactory(TaskDao taskDao) {

        this.taskDao = taskDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(taskDao);
    }
}
