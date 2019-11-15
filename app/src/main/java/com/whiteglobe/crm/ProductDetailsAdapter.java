package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailsAdapter  extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {
    Context context;
    List<ProductDetailsGS> productdata;

    public ProductDetailsAdapter(List<ProductDetailsGS> getDataAdapter, Context context){

        super();

        this.productdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ProductDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_details, parent, false);

        ProductDetailsAdapter.ViewHolder viewHolder = new ProductDetailsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductDetailsAdapter.ViewHolder holder, int position) {

        ProductDetailsGS productDetailsGS =  productdata.get(position);

        holder.txtCardProductName.setText(productDetailsGS.getProdName());
        holder.txtCardProductDescr.setText(productDetailsGS.getProdDescr());
        holder.txtCardProductBrand.setText(productDetailsGS.getProdBrand());
        holder.txtCardProductContent.setText(productDetailsGS.getProdContent());

        Picasso.get().load(WebName.imgurl+"product_images/"+productDetailsGS.getProdImage()).into(holder.imgCardProductImage);
    }

    @Override
    public int getItemCount() {
        return productdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardProductName,txtCardProductDescr,txtCardProductBrand,txtCardProductContent;
        public AppCompatImageView imgCardProductImage;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardProductName = itemView.findViewById(R.id.txtCardProductName) ;
            txtCardProductDescr = itemView.findViewById(R.id.txtCardProductDescr) ;
            txtCardProductBrand = itemView.findViewById(R.id.txtCardProductBrand) ;
            txtCardProductContent = itemView.findViewById(R.id.txtCardProductContent) ;
            imgCardProductImage = itemView.findViewById(R.id.imgCardProductImage) ;
        }
    }
}