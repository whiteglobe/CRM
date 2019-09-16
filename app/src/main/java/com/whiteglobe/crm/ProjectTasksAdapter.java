package com.whiteglobe.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectTasksAdapter extends RecyclerView.Adapter<ProjectTasksAdapter.ViewHolder> {
    Context context;
    List<ProjectTasksGS> projecttasksdata;

    public ProjectTasksAdapter(List<ProjectTasksGS> getDataAdapter, Context context){

        super();

        this.projecttasksdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProjectTasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_project_tasks, parent, false);

        ProjectTasksAdapter.ViewHolder viewHolder = new ProjectTasksAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectTasksAdapter.ViewHolder holder, int position) {

        ProjectTasksGS getProjectTasksData =  projecttasksdata.get(position);

        holder.txtCardProjectTaskTitle.setText(getProjectTasksData.getTaskTitle());
        holder.txtCardProjectTaskDate.setText(getProjectTasksData.getTaskDate());
        if(getProjectTasksData.getTaskStatus().equals("Completed"))
        {
            holder.txtCardProjectTaskStatus.setTextColor(Color.GREEN);
        }
        else if(getProjectTasksData.getTaskStatus().equals("Pending"))
        {
            holder.txtCardProjectTaskStatus.setTextColor(Color.RED);
        }
        else if(getProjectTasksData.getTaskStatus().equals("In Progress"))
        {
            holder.txtCardProjectTaskStatus.setTextColor(Color.rgb(204,204,0));
        }
        holder.txtCardProjectTaskStatus.setText(getProjectTasksData.getTaskStatus());
        holder.txtTaskId = getProjectTasksData.getTaskID();
    }

    @Override
    public int getItemCount() {

        return projecttasksdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardProjectTaskTitle,txtCardProjectTaskDate,txtCardProjectTaskStatus;
        public int txtTaskId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardProjectTaskTitle = itemView.findViewById(R.id.txtCardProjectTaskTitle) ;
            txtCardProjectTaskDate = itemView.findViewById(R.id.txtCardProjectTaskDate) ;
            txtCardProjectTaskStatus = itemView.findViewById(R.id.txtCardProjectTaskStatus) ;
        }
    }
}
