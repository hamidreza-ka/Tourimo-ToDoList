package com.example.mvvmtodolist.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmtodolist.model.AppDatabase;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public MainViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(AppDatabase.getAppDatabase(context).getTaskDao());
    }
}
