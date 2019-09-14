package com.whiteglobe.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectImagesAdapter extends RecyclerView.Adapter<ProjectImagesAdapter.ViewHolder> {
    Context context;
    List<ProjectImageGS> projectimagesdata;

    public ProjectImagesAdapter(List<ProjectImageGS> getDataAdapter, Context context){

        super();

        this.projectimagesdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProjectImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_project_images, parent, false);

        ProjectImagesAdapter.ViewHolder viewHolder = new ProjectImagesAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProjectImagesAdapter.ViewHolder holder, int position) {

        ProjectImageGS getProjectImageData =  projectimagesdata.get(position);

        holder.txtCardProjectImageRemarks.setText(getProjectImageData.getImgRemarks());
        holder.txtCardProjectImageDate.setText(getProjectImageData.getImgDate());

        Picasso.get().load(WebName.imgurl+"project_images/"+getProjectImageData.getProjectUnique()+"/"+getProjectImageData.getImgName()).into(holder.imgCardProjectImage);
    }

    @Override
    public int getItemCount() {
        return projectimagesdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardProjectImageRemarks,txtCardProjectImageDate;
        public AppCompatImageView imgCardProjectImage;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardProjectImageRemarks = itemView.findViewById(R.id.txtCardProjectImageRemarks) ;
            txtCardProjectImageDate = itemView.findViewById(R.id.txtCardProjectImageDate) ;
            imgCardProjectImage = itemView.findViewById(R.id.imgCardProjectImage) ;
        }
    }
}
