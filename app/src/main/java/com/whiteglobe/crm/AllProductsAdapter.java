package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {
    Context context;
    List<AllProductsGS> allProductsData;

    public AllProductsAdapter(List<AllProductsGS> getDataAdapter, Context context){

        super();

        this.allProductsData = getDataAdapter;

        this.context = context;
    }

    @Override
    public AllProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_product_categories, parent, false);

        AllProductsAdapter.ViewHolder viewHolder = new AllProductsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AllProductsAdapter.ViewHolder holder, int position) {

        AllProductsGS getProductCatData =  allProductsData.get(position);

        holder.txtProductCategory.setText(getProductCatData.getCat_name());
        holder.txtCatId = getProductCatData.getCat_id();
    }

    @Override
    public int getItemCount() {

        return allProductsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtProductCategory;
        public int txtCatId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtProductCategory = itemView.findViewById(R.id.txtProductCategory) ;
        }
    }
}