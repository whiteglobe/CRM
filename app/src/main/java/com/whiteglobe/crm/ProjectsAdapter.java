package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {
    Context context;
    List<ProjectGS> projectsdata;

    public ProjectsAdapter(List<ProjectGS> getDataAdapter, Context context){

        super();

        this.projectsdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_all_projects, parent, false);

        ProjectsAdapter.ViewHolder viewHolder = new ProjectsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectsAdapter.ViewHolder holder, int position) {

        ProjectGS getProjectsData =  projectsdata.get(position);

        holder.txtCardProjectName.setText(getProjectsData.getProjectName());
        holder.txtCardProjectOverview.setText(getProjectsData.getProjectOverview());
        holder.txtProjectSiteClearenceDate.setText(getProjectsData.getProjectClearenceDate());
        holder.txtProjectStartDate.setText(getProjectsData.getProjectStartDate());
        holder.txtProjectEndDate.setText(getProjectsData.getProjectEndDate());
        holder.txtProjectStatus.setText(getProjectsData.getProjectStatus());
        holder.txtCardProjectUnique = getProjectsData.getProjectUnique();
    }

    @Override
    public int getItemCount() {

        return projectsdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardProjectName,txtCardProjectOverview,txtProjectSiteClearenceDate,txtProjectStartDate,txtProjectEndDate,txtProjectStatus;
        public String txtCardProjectUnique;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardProjectName = itemView.findViewById(R.id.txtCardProjectName) ;
            txtCardProjectOverview = itemView.findViewById(R.id.txtCardProjectOverview) ;
            txtProjectSiteClearenceDate = itemView.findViewById(R.id.txtProjectSiteClearenceDate) ;
            txtProjectStartDate = itemView.findViewById(R.id.txtProjectStartDate) ;
            txtProjectEndDate = itemView.findViewById(R.id.txtProjectEndDate) ;
            txtProjectStatus = itemView.findViewById(R.id.txtProjectStatus) ;
        }
    }
}
