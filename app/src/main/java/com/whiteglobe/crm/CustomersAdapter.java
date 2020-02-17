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

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> implements Filterable {
    Context context;
    List<CustomersGS> allCustomerData;
    List<CustomersGS> allCustomerDataFiltered;

    public CustomersAdapter(List<CustomersGS> getDataAdapter, Context context){

        super();

        this.allCustomerData = getDataAdapter;
        this.allCustomerDataFiltered = getDataAdapter;

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

        CustomersGS customersGS =  allCustomerDataFiltered.get(position);

        holder.txtCustomerName.setText(customersGS.getCustName());
        holder.txtCardCustomerEmail.setText(customersGS.getCustEmail());
        holder.txtCardCustomerPhone.setText(customersGS.getCustPhone());
        holder.txtCustId = customersGS.getCustID();
    }

    @Override
    public int getItemCount() {

        return allCustomerDataFiltered.size();
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allCustomerDataFiltered = allCustomerData;
                } else {
                    List<CustomersGS> filteredList = new ArrayList<>();
                    for (CustomersGS row : allCustomerData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustName().toLowerCase().contains(charString.toLowerCase()) || row.getCustPhone().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    allCustomerDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allCustomerDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allCustomerDataFiltered = (ArrayList<CustomersGS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}