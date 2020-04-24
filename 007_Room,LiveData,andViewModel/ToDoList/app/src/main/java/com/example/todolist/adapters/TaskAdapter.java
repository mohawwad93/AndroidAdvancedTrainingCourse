package com.example.todolist.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.database.Task;
import com.example.todolist.utils.Utils;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskVH> {

    private List<Task> mTaskList;
    private LayoutInflater mInflater;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public TaskAdapter(Context context, ItemClickListener itemClickListener){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
    }

    public Task getTask(int position){

        return mTaskList.get(position);
    }

    public void setTasks(List<Task> taskEntries){
        mTaskList = taskEntries;
        notifyDataSetChanged();
    }


    public void deleteTask(Task task){
        mTaskList.remove(task);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_task, parent, false);
        return new TaskVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskVH holder, int position) {

        Task task = mTaskList.get(position);
        holder.nameTextView.setText(task.getName());
        holder.dateTextView.setText(Utils.formatDate(task.getDateMillisecond()));
        holder.priorityTextView.setText(String.valueOf(task.getPriority()));
        GradientDrawable gradientDrawable = (GradientDrawable) holder.priorityTextView.getBackground();
        gradientDrawable.setColor(getPriorityColor(task.getPriority()));

    }

    @Override
    public int getItemCount() {
        if(mTaskList != null){
            return mTaskList.size();
        }
        return 0;
    }

    class TaskVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTextView;
        TextView dateTextView;
        TextView priorityTextView;

        TaskVH(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameTextView = itemView.findViewById(R.id.name_textview);
            dateTextView = itemView.findViewById(R.id.date_textview);
            priorityTextView = itemView.findViewById(R.id.priority_textview);
        }

        @Override
        public void onClick(View v) {
            Task tast = mTaskList.get(getAdapterPosition());
           mItemClickListener.onItemClickListener(tast);
        }
    }

    private int getPriorityColor(int priority){

        switch (priority){
            case 1:
                return ContextCompat.getColor(mContext, R.color.materialRed);
            case 2:
                return ContextCompat.getColor(mContext, R.color.materialOrange);
            case 3:
                return ContextCompat.getColor(mContext, R.color.materialYellow);
        }

        return 0;
    }

    public interface ItemClickListener{
        void onItemClickListener(Task task);
    }
}
