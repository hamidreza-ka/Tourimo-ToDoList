package com.example.mvvmtodolist.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtodolist.R;
import com.example.mvvmtodolist.detail.TaskDetailActivity;
import com.example.mvvmtodolist.model.AppDatabase;
import com.example.mvvmtodolist.model.Task;


import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements TaskAdapter.onTaskEventListener {
    private static final int REQUEST_CODE = 1001;
    public static final int RESULT_CODE_ADD_TASK = 1002;
    public static final int RESULT_CODE_UPDATE_TASK = 1003;
    public static final int RESULT_CODE_DELETE_TASK = 1004;
    public static final String KEY_CODE_EXTRA = "task";
    private MainViewModel viewModel;
    private TaskAdapter taskAdapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private Disposable disposable;
    private ProgressBar progressBar;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this, new MainViewModelFactory(AppDatabase.getAppDatabase(getApplicationContext()).getTaskDao())).get(MainViewModel.class);
        taskAdapter = new TaskAdapter(this, this);
        emptyState = findViewById(R.id.emptyState);

        progressBar = findViewById(R.id.main_progress_bar);

        viewModel.getShowProgressBar().observe(this, showBoolean -> progressBar.setVisibility(showBoolean ? View.VISIBLE : View.GONE));


        View btnAddTask = findViewById(R.id.addNewTaskBtn);
        btnAddTask.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TaskDetailActivity.class)));

        View btnClearTasks = findViewById(R.id.btn_main_deleteAll);
        btnClearTasks.setOnClickListener(v -> {
            viewModel.clearTasks();
            setEmptyState(true);
        });

        EditText searchEt = findViewById(R.id.et_search_main);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                viewModel.searchTasks(s.toString()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tasks -> taskAdapter.showTasks(tasks));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        viewModel.getTasks().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Task>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(List<Task> tasks) {

                taskAdapter.showTasks(tasks);
                recyclerView.setAdapter(taskAdapter);
                setEmptyState(taskAdapter.getItemCount() == 0);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "db dose not valid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                if (disposable != null)
                disposable.dispose();
            }
        });


    }

    public void setEmptyState(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClickListener(Task task) {
        boolean isChecked = !task.getChecked();
        task.setChecked(isChecked);
        taskAdapter.updateTask(task);
        viewModel.updateTask(task);
    }

    @Override
    public void onItemLongClick(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
        intent.putExtra(KEY_CODE_EXTRA, task);
        startActivity(intent);
    }
}