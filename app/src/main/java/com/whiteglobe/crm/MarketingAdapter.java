package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.ViewHolder> {
    Context context;
    List<MarketingGS> marketingdata;

    public MarketingAdapter(List<MarketingGS> getDataAdapter, Context context){

        super();

        this.marketingdata = getDataAdapter;

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

        MarketingGS getMarketingData =  marketingdata.get(position);

        holder.txtCardMarketingName.setText(getMarketingData.getMktName());
        holder.txtCardMarketingPhone.setText(getMarketingData.getMktPhone());
        holder.txtCardMarketingAddress.setText(getMarketingData.getMktAddress());
        holder.marktID = getMarketingData.getMktID();
    }

    @Override
    public int getItemCount() {

        return marketingdata.size();
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
}
