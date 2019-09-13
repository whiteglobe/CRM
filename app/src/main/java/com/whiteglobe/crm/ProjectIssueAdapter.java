package com.whiteglobe.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectIssueAdapter extends RecyclerView.Adapter<ProjectIssueAdapter.ViewHolder> {
    Context context;
    List<ProjectIssueGs> projectissuesdata;

    public ProjectIssueAdapter(List<ProjectIssueGs> getDataAdapter, Context context){

        super();

        this.projectissuesdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProjectIssueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_project_issues, parent, false);

        ProjectIssueAdapter.ViewHolder viewHolder = new ProjectIssueAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectIssueAdapter.ViewHolder holder, int position) {

        ProjectIssueGs getProjectIssuesData =  projectissuesdata.get(position);

        holder.txtCardProjectIssueTitle.setText(getProjectIssuesData.getIssueTitle());
        holder.txtCardProjectIssueDate.setText(getProjectIssuesData.getIssueDate());
        if(getProjectIssuesData.getIssueStatus().equals("Solved"))
        {
            holder.txtCardProjectIssueStatus.setTextColor(Color.GREEN);
        }
        else if(getProjectIssuesData.getIssueStatus().equals("New"))
        {
            holder.txtCardProjectIssueStatus.setTextColor(Color.RED);
        }
        holder.txtCardProjectIssueStatus.setText(getProjectIssuesData.getIssueStatus());
        holder.txtTaskId = getProjectIssuesData.getIssueId();
    }

    @Override
    public int getItemCount() {

        return projectissuesdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardProjectIssueTitle,txtCardProjectIssueDate,txtCardProjectIssueStatus;
        public int txtTaskId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardProjectIssueTitle = itemView.findViewById(R.id.txtCardProjectIssueTitle) ;
            txtCardProjectIssueDate = itemView.findViewById(R.id.txtCardProjectIssueDate) ;
            txtCardProjectIssueStatus = itemView.findViewById(R.id.txtCardProjectIssueStatus) ;
        }
    }
}
