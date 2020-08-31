package com.example.mvvmtodolist.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mvvmtodolist.R;
import com.example.mvvmtodolist.main.MainActivity;
import com.example.mvvmtodolist.model.AppDatabase;
import com.example.mvvmtodolist.model.Task;
import com.google.android.material.snackbar.Snackbar;

public class TaskDetailActivity extends AppCompatActivity {
    private TaskViewModel viewModel;
    private int selectedImportance = Task.IMPORTANCE_NORMAL;
    private ImageView lastSelectedImportanceIv;
    private EditText taskTitle;
    private View btnDeleteTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        viewModel = new TaskViewModel(AppDatabase.getAppDatabase(this).getTaskDao(), getIntent().getParcelableExtra(MainActivity.KEY_CODE_EXTRA));
        taskTitle = findViewById(R.id.et_edit_task);

        View btnBack = findViewById(R.id.iv_backEdit);
        btnBack.setOnClickListener(v -> finish());

        View btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnSaveChanges.setOnClickListener(v -> {
            if (taskTitle.getText().toString().isEmpty()) {
                showError();
                return;
            }

            viewModel.saveTask(selectedImportance, taskTitle.getText().toString());
            if (viewModel.getResultTask() == "add") {
                returnResult(MainActivity.RESULT_CODE_ADD_TASK, viewModel.getAddTask());

            } else if (viewModel.getResultTask() == "update") {
                returnResult(MainActivity.RESULT_CODE_UPDATE_TASK, viewModel.getUpdateTask());
            }

        });

        btnDeleteTask = findViewById(R.id.iv_delete_task);
        btnDeleteTask.setVisibility(viewModel.isSetDeleteBtnVisibility() ? View.VISIBLE : View.GONE);
        btnDeleteTask.setOnClickListener(v -> {
            viewModel.deleteTask();

            if (viewModel.getResultTask() == "delete") {
                returnResult(MainActivity.RESULT_CODE_DELETE_TASK, viewModel.getUpdateTask());
            }
        });


        View normalImportanceBtn = findViewById(R.id.normal_importance);
        lastSelectedImportanceIv = normalImportanceBtn.findViewById(R.id.ic_importance_normal);

        final View highImportanceBtn = findViewById(R.id.high_importance);
        highImportanceBtn.setOnClickListener(v -> {
            if (selectedImportance != Task.IMPORTANCE_HIGH) {
                lastSelectedImportanceIv.setImageResource(0);
                ImageView imageView = v.findViewById(R.id.ic_importance_high);
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_HIGH;

                lastSelectedImportanceIv = imageView;
            }
        });

        normalImportanceBtn.setOnClickListener(v -> {
            if (selectedImportance != Task.IMPORTANCE_NORMAL) {
                lastSelectedImportanceIv.setImageResource(0);
                ImageView imageView = v.findViewById(R.id.ic_importance_normal);
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_NORMAL;

                lastSelectedImportanceIv = imageView;
            }
        });

        final View lowImportanceBtn = findViewById(R.id.low_importance);
        lowImportanceBtn.setOnClickListener(v -> {
            if (selectedImportance != Task.IMPORTANCE_LOW) {
                lastSelectedImportanceIv.setImageResource(0);
                ImageView imageView = v.findViewById(R.id.ic_importance_low);
                imageView.setImageResource(R.drawable.ic_check_white_24dp);
                selectedImportance = Task.IMPORTANCE_LOW;

                lastSelectedImportanceIv = imageView;
            }
        });

        if (viewModel.isShowTask())
            showTask(getIntent().getParcelableExtra(MainActivity.KEY_CODE_EXTRA));

    }


    public void showTask(Task task) {
        try {
            taskTitle.setText(task.getTitle());
            switch (task.getPriority()) {
                case Task.IMPORTANCE_LOW:
                    findViewById(R.id.low_importance).performClick();
                    break;
                case Task.IMPORTANCE_NORMAL:
                    findViewById(R.id.normal_importance).performClick();
                    break;
                case Task.IMPORTANCE_HIGH:
                    findViewById(R.id.high_importance).performClick();
                    break;
            }
        } catch (Exception e) {
        }

    }


    public void setDeleteButtonVisibility(boolean visible) {
        try {
            btnDeleteTask.setVisibility(visible ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
        }

    }


    public void showError() {
        Snackbar.make(findViewById(R.id.task_detail_root), "Plz Enter Title Task :)", Snackbar.LENGTH_SHORT).show();
    }


    public void returnResult(int resultCode, Task task) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.KEY_CODE_EXTRA, task);
        setResult(resultCode, intent);
        finish();
    }

}