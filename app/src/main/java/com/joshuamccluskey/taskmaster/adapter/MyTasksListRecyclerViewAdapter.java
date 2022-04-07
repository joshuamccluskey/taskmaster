package com.joshuamccluskey.taskmaster.adapter;

import static com.joshuamccluskey.taskmaster.activity.MyTasksActivity.TASK_ID_TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.joshuamccluskey.taskmaster.R;
import com.joshuamccluskey.taskmaster.activity.EditTaskActivity;

import java.util.List;

public class MyTasksListRecyclerViewAdapter extends RecyclerView.Adapter<MyTasksListRecyclerViewAdapter.MyTasksListViewHolder> {
    List<Task> tasksList;
    Context gettingActivity;


    public MyTasksListRecyclerViewAdapter(List<Task> tasksList, Context gettingActivity) {
        this.tasksList = tasksList;
        this.gettingActivity = gettingActivity;
    }

    @NonNull
    @Override
    public MyTasksListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_tasks_list, parent, false);

        return new MyTasksListViewHolder(taskListFragment);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyTasksListViewHolder holder, int position) {
        TextView taskFragmentTextView = holder.itemView.findViewById(R.id.taskFragmentTextView);
        Task task = tasksList.get(position);
        taskFragmentTextView.setText( position+1 +" "+ task.getTitle() +
                ": " + task.getState());

        View myTaskViewHolder = holder.itemView;
        myTaskViewHolder.setOnClickListener(view -> {
            Intent goToEditTaskActivityIntent = new Intent(gettingActivity, EditTaskActivity.class);
            goToEditTaskActivityIntent.putExtra(TASK_ID_TAG, task.getId());
            gettingActivity.startActivity(goToEditTaskActivityIntent);
        });


    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public static class MyTasksListViewHolder extends RecyclerView.ViewHolder{

        public MyTasksListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
