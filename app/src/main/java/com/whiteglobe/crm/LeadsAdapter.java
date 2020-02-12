package com.whiteglobe.crm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.ViewHolder> {
    Context context;
    List<LeadGS> leadsdata;

    public LeadsAdapter(List<LeadGS> getDataAdapter, Context context){

        super();

        this.leadsdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_leads, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final LeadGS getLeadsData =  leadsdata.get(position);

        holder.leadName.setText(getLeadsData.getName());
        holder.leadTitle.setText(getLeadsData.getTitle());
        holder.leadPhone.setText(getLeadsData.getPhoneno());
        /*holder.leadPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+getLeadsData.getPhoneno()));
                context.startActivity(intent);
            }
        });*/
        holder.leadID = getLeadsData.getLeadID();
    }

    @Override
    public int getItemCount() {

        return leadsdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView leadName,leadTitle,leadPhone;
        public int leadID;


        public ViewHolder(View itemView) {

            super(itemView);

            leadName = itemView.findViewById(R.id.txtCardName) ;
            leadTitle = itemView.findViewById(R.id.txtCardTitle) ;
            leadPhone = itemView.findViewById(R.id.txtCardPhone) ;
        }
    }
}
