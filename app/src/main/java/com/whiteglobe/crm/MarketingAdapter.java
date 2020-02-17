package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.ViewHolder> implements Filterable {
    Context context;
    List<MarketingGS> marketingdata;
    List<MarketingGS> marketingdataFiltered;

    public MarketingAdapter(List<MarketingGS> getDataAdapter, Context context){

        super();

        this.marketingdata = getDataAdapter;
        this.marketingdataFiltered = getDataAdapter;

        this.context = context;
    }

    @Override
    public MarketingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_marketing_leads, parent, false);

        MarketingAdapter.ViewHolder viewHolder = new MarketingAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MarketingAdapter.ViewHolder holder, int position) {

        MarketingGS getMarketingData =  marketingdataFiltered.get(position);

        holder.txtCardMarketingName.setText(getMarketingData.getMktName());
        holder.txtCardMarketingPhone.setText(getMarketingData.getMktPhone());
        holder.txtCardMarketingAddress.setText(getMarketingData.getMktAddress());
        holder.marktID = getMarketingData.getMktID();
    }

    @Override
    public int getItemCount() {

        return marketingdataFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardMarketingName,txtCardMarketingPhone,txtCardMarketingAddress;
        public int marktID;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardMarketingName = itemView.findViewById(R.id.txtCardMarketingName) ;
            txtCardMarketingPhone = itemView.findViewById(R.id.txtCardMarketingPhone) ;
            txtCardMarketingAddress = itemView.findViewById(R.id.txtCardMarketingAddress) ;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    marketingdataFiltered = marketingdata;
                } else {
                    List<MarketingGS> filteredList = new ArrayList<>();
                    for (MarketingGS row : marketingdata) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getMktName().toLowerCase().contains(charString.toLowerCase()) || row.getMktPhone().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    marketingdataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = marketingdataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                marketingdataFiltered = (ArrayList<MarketingGS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
