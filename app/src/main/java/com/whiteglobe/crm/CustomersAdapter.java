package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {
    Context context;
    List<CustomersGS> allCustomerData;

    public CustomersAdapter(List<CustomersGS> getDataAdapter, Context context){

        super();

        this.allCustomerData = getDataAdapter;

        this.context = context;
    }

    @Override
    public CustomersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_customers, parent, false);

        CustomersAdapter.ViewHolder viewHolder = new CustomersAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomersAdapter.ViewHolder holder, int position) {

        CustomersGS customersGS =  allCustomerData.get(position);

        holder.txtCustomerName.setText(customersGS.getCustName());
        holder.txtCardCustomerEmail.setText(customersGS.getCustEmail());
        holder.txtCardCustomerPhone.setText(customersGS.getCustPhone());
        holder.txtCustId = customersGS.getCustID();
    }

    @Override
    public int getItemCount() {

        return allCustomerData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCustomerName,txtCardCustomerEmail,txtCardCustomerPhone;
        public int txtCustId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCustomerName = itemView.findViewById(R.id.txtCustomerName) ;
            txtCardCustomerEmail = itemView.findViewById(R.id.txtCardCustomerEmail) ;
            txtCardCustomerPhone = itemView.findViewById(R.id.txtCardCustomerPhone) ;
        }
    }
}