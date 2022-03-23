package com.joshuamccluskey.taskmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.model.Task;

import java.util.List;

public class MyTasksListRecyclerViewAdapter extends RecyclerView.Adapter {
    List<Task> taskList;
    Context gettingActivity;

    public MyTasksListRecyclerViewAdapter(List<Task> taskList, Context gettingActivity) {
        this.taskList = taskList;
        this.gettingActivity = gettingActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_tasks_list, parent, false);

        return new MyTasksListViewHolder(taskListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class MyTasksListViewHolder extends RecyclerView.ViewHolder{

        public MyTasksListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
