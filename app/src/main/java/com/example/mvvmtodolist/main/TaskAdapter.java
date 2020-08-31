package com.example.mvvmtodolist.main;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvvmtodolist.R;
import com.example.mvvmtodolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private onTaskEventListener eventListener;
    private Drawable highImportantDrawable;
    private Drawable normalImportantDrawable;
    private Drawable lowImportantDrawable;


    public TaskAdapter(Context context,onTaskEventListener eventListener) {
        this.eventListener = eventListener;
        highImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_high_rect, null);
        normalImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_normal_rect, null);
        lowImportantDrawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.shape_importance_low_rect, null);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.BindTask(tasks.get(position));
    }

    public void addTask(Task task) {
        switch (task.getPriority()){
            case Task.IMPORTANCE_LOW:
                tasks.add(0, task);
                notifyItemInserted(0);
                break;
            case Task.IMPORTANCE_NORMAL:
                tasks.add(0, task);
                notifyItemInserted(0);
                break;
            case Task.IMPORTANCE_HIGH:
                tasks.add(task);
                notifyItemInserted(tasks.size());
                break;
        }

    }

    public void showTasks(List<Task> tasks) {
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    public void updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void deleteItem(Task task) {
        int result = tasks.indexOf(task);
        tasks.remove(result);
        notifyItemRemoved(result);
    }

    public void deleteItems(){
        this.tasks.clear();
        notifyDataSetChanged();
    }

    public void searchItem(List<Task> tasks){
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private ImageView isChecked;
        private TextView title;
        private View importance;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            isChecked = itemView.findViewById(R.id.item_iv_check);
            title = itemView.findViewById(R.id.item_tv);
            importance = itemView.findViewById(R.id.item_importanceView);



        }

        public void BindTask(final Task task) {

            title.setText(task.getTitle());

            if (task.getChecked()){
                    isChecked.setBackgroundResource(R.drawable.shape_checkbox_checked);
                    isChecked.setImageResource(R.drawable.ic_check_white_24dp);
                    title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                isChecked.setBackgroundResource(R.drawable.shape_checkbox_default);
                isChecked.setImageResource(0);
                title.setPaintFlags(0);
            }

            itemView.setOnClickListener(v -> eventListener.onItemClickListener(task));

            itemView.setOnLongClickListener(v -> {
                eventListener.onItemLongClick(task);
                return false;
            });


            switch (task.getPriority()){
                case Task.IMPORTANCE_LOW:
                    importance.setBackground(lowImportantDrawable);
                    break;
                case Task.IMPORTANCE_NORMAL:
                    importance.setBackground(normalImportantDrawable);
                    break;
                case Task.IMPORTANCE_HIGH:
                    importance.setBackground(highImportantDrawable);
                    break;
            }
        }

    }

    public interface onTaskEventListener {

        void onItemClickListener(Task task);

        void onItemLongClick(Task task);

    }
}
