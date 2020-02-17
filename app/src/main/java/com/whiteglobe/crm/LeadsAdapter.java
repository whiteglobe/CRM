package com.whiteglobe.crm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.ViewHolder> implements Filterable {
    Context context;
    List<LeadGS> leadsdata;
    List<LeadGS> leadsdataFiltered;

    public LeadsAdapter(List<LeadGS> getDataAdapter, Context context){

        super();

        this.leadsdata = getDataAdapter;
        this.leadsdataFiltered = getDataAdapter;

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

        final LeadGS getLeadsData =  leadsdataFiltered.get(position);

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

        return leadsdataFiltered.size();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    leadsdataFiltered = leadsdata;
                } else {
                    List<LeadGS> filteredList = new ArrayList<>();
                    for (LeadGS row : leadsdata) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhoneno().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    leadsdataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = leadsdataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                leadsdataFiltered = (ArrayList<LeadGS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
