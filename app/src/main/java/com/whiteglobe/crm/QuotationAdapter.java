package com.whiteglobe.crm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuotationAdapter extends RecyclerView.Adapter<QuotationAdapter.ViewHolder> implements Filterable {
    Context context;
    List<QuotationGS> quotdata;
    List<QuotationGS> quotationListFiltered;

    public QuotationAdapter(List<QuotationGS> getDataAdapter, Context context){

        super();

        this.quotdata = getDataAdapter;

        this.quotationListFiltered = getDataAdapter;

        this.context = context;
    }

    @Override
    public QuotationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_quotationlist, parent, false);

        QuotationAdapter.ViewHolder viewHolder = new QuotationAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuotationAdapter.ViewHolder holder, int position) {

        QuotationGS quotationGS =  quotationListFiltered.get(position);

        holder.txtCardQuotNo.setText(quotationGS.getQuotNo());
        holder.txtCardPartyName.setText(capitalize(quotationGS.getPartyName()).trim().replaceAll("\\s{2,}", " "));
        holder.txtCardQuotDate.setText(quotationGS.getQuotDate());
        holder.txtCardLead.setText(capitalize(quotationGS.getLead()).trim().replaceAll("\\s{2,}", " "));
        holder.txtCardAmount.setText(context.getResources().getString(R.string.Rs)+" "+quotationGS.getAmt());
    }

    @Override
    public int getItemCount() {
        return quotationListFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardQuotNo,txtCardPartyName,txtCardQuotDate,txtCardLead,txtCardAmount;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardQuotNo = itemView.findViewById(R.id.txtCardQuotNo);
            txtCardPartyName = itemView.findViewById(R.id.txtCardPartyName);
            txtCardQuotDate = itemView.findViewById(R.id.txtCardQuotDate);
            txtCardLead = itemView.findViewById(R.id.txtCardLead);
            txtCardAmount = itemView.findViewById(R.id.txtCardAmount);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    quotationListFiltered = quotdata;
                } else {
                    List<QuotationGS> filteredList = new ArrayList<>();
                    for (QuotationGS row : quotdata) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPartyName().toLowerCase().contains(charString.toLowerCase()) || row.getQuotNo().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    quotationListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = quotationListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                quotationListFiltered = (ArrayList<QuotationGS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}