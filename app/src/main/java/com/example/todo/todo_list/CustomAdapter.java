package com.example.todo.todo_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Task> taskList;
    private Context mContext;

    CustomAdapter(Context context) {
        this.mContext = context;
        taskList = new ArrayList<>();

    }


    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View view =
                inflater.inflate(R.layout.custom_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder holder, int position) {

        Task task = taskList.get(position);
        holder.tasks.setText(task.getTodo());
        holder.tv_priority.setText("Priority on "+task.getPriority());
        holder.tv_start_date.setText(task.getStartDate());
        holder.tv_end_date.setText(task.getEndDate());
        holder.tv_note.setText("Notes: "+task.getNote());
        String assignedBy=task.getAssignby().get(0).getCustomerName();
        holder.tv_assigned_by.setText("AssignedBy: "+assignedBy);

        ArrayList<String> list = new ArrayList<String>();
        for (User user : task.getAssignto())
            list.add(user.getCustomerName());

        ArrayAdapter<String> assignedAdapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item, list);
        assignedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerAssigned.setSelection(0);
        holder.spinnerAssigned.setSelected(false);
        holder.spinnerAssigned.setAdapter(assignedAdapter);
        holder.spinnerAssigned.setOnItemSelectedListener(null);


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public void addTasks(ArrayList<Task> taskList) {
        if (taskList.size() != 0) {
            this.taskList.clear();
            this.taskList.addAll(taskList);
            notifyDataSetChanged();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tasks;
        Spinner spinnerAssigned;
        TextView tv_note;
        TextView tv_start_date;
        TextView tv_end_date;
        TextView tv_priority;
        TextView tv_assigned_by;



        public View layout;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            tasks = itemView.findViewById(R.id.task);
            spinnerAssigned=itemView.findViewById(R.id.spinner_assigned);
            tv_note= itemView.findViewById(R.id.note);
            tv_start_date =itemView.findViewById(R.id.startDate);
            tv_end_date= itemView.findViewById(R.id.endDate);
            tv_priority= itemView.findViewById(R.id.priority);
            tv_assigned_by=itemView.findViewById(R.id.text_assigned_by);

        }
    }
}
