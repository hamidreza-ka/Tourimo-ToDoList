package com.example.mvvmtodolist.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmtodolist.model.Task;
import com.example.mvvmtodolist.model.TaskDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;


public class MainViewModel extends ViewModel {
    private TaskDao taskDao;
    private MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();

    public MainViewModel(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public Observable<List<Task>> getTasks() {
        showProgressBar.setValue(true);
        return taskDao.getTasks()
               .delay(2, TimeUnit.SECONDS)
                .doOnEach(tasks -> showProgressBar.postValue(false));


    }

    public void clearTasks() {
        taskDao.deleteAll();
    }

    public Maybe<List<Task>> searchTasks(String query) {
        return taskDao.search(query);
    }

    public void updateTask(Task task) {
        taskDao.update(task);
    }

    public MutableLiveData<Boolean> getShowProgressBar() {
        return showProgressBar;
    }
}
