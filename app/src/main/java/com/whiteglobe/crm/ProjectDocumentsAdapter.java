package com.whiteglobe.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProjectDocumentsAdapter extends RecyclerView.Adapter<ProjectDocumentsAdapter.ViewHolder> {
    Context context;
    List<ProjectDocumentsGS> projectdocumentsdata;

    public ProjectDocumentsAdapter(List<ProjectDocumentsGS> getDataAdapter, Context context){

        super();

        this.projectdocumentsdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProjectDocumentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_project_documents, parent, false);

        ProjectDocumentsAdapter.ViewHolder viewHolder = new ProjectDocumentsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectDocumentsAdapter.ViewHolder holder, int position) {

        ProjectDocumentsGS projectDocumentsGS =  projectdocumentsdata.get(position);

        holder.txtDocumentName.setText(projectDocumentsGS.getDocName());
        holder.txtCardDocumentDate.setText(projectDocumentsGS.getDocDate());
        holder.txtCardDocumentUploadedBy.setText(projectDocumentsGS.getDocUploadedBy());
        holder.txtDocId = projectDocumentsGS.getDocId();
    }

    @Override
    public int getItemCount() {

        return projectdocumentsdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtDocumentName,txtCardDocumentDate,txtCardDocumentUploadedBy;
        public int txtDocId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtDocumentName = itemView.findViewById(R.id.txtDocumentName) ;
            txtCardDocumentDate = itemView.findViewById(R.id.txtCardDocumentDate) ;
            txtCardDocumentUploadedBy = itemView.findViewById(R.id.txtCardDocumentUploadedBy) ;
        }
    }
}