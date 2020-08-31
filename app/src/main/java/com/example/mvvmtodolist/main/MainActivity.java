package com.example.mvvmtodolist.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtodolist.R;
import com.example.mvvmtodolist.databinding.ActivityMainBinding;
import com.example.mvvmtodolist.detail.TaskDetailActivity;
import com.example.mvvmtodolist.model.AppDatabase;
import com.example.mvvmtodolist.model.Task;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
    private ProgressBar progressBar;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new MainViewModel(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter = new TaskAdapter(this, this);
        emptyState = findViewById(R.id.emptyState);

        progressBar = findViewById(R.id.main_progress_bar);

        viewModel.getShowProgressBar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean showBoolean) {
             progressBar.setVisibility(showBoolean? View.VISIBLE : View.GONE);
            }
        });


//        viewModel.getProgressBarSubject()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(showBoolean -> progressBar.setVisibility(showBoolean ? View.VISIBLE : View.GONE));

        View btnAddTask = findViewById(R.id.addNewTaskBtn);
        btnAddTask.setOnClickListener(v -> {
            startActivityForResult(new Intent(MainActivity.this, TaskDetailActivity.class), REQUEST_CODE);
        });

        View btnClearTasks = findViewById(R.id.btn_main_deleteAll);
        btnClearTasks.setOnClickListener(v -> {
            viewModel.clearTasks();
            taskAdapter.deleteItems();
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
                        .subscribe(tasks -> taskAdapter.searchItem(tasks));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        viewModel.getTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tasks -> {
                    taskAdapter.showTasks(tasks);
                    recyclerView.setAdapter(taskAdapter);
                    setEmptyState(taskAdapter.getItemCount() == 0);
                });


    }

    public void setEmptyState(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if ((resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK || resultCode == RESULT_CODE_DELETE_TASK) && data != null) {
                Task task = data.getParcelableExtra(KEY_CODE_EXTRA);

                if (task != null) {
                    if (resultCode == RESULT_CODE_ADD_TASK) {
                        taskAdapter.addTask(task);

                    } else if (resultCode == RESULT_CODE_UPDATE_TASK) {
                        taskAdapter.updateTask(task);
                    } else
                        taskAdapter.deleteItem(task);

                    setEmptyState(false);
                }


            }

        }
    }

    @Override
    public void onItemClickListener(Task task) {
        boolean isChecked = !task.getChecked();
        task.setChecked(isChecked);
        viewModel.updateTask(task);
        taskAdapter.updateTask(task);
    }

    @Override
    public void onItemLongClick(Task task) {
        Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
        intent.putExtra(KEY_CODE_EXTRA, task);
        startActivityForResult(intent, REQUEST_CODE);
    }
}