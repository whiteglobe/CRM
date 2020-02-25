package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExhibitionAdapter extends RecyclerView.Adapter<ExhibitionAdapter.ViewHolder> implements Filterable {

    Context context;
    List<ExhibitionGS> exhibitdata;
    List<ExhibitionGS> exhibitListFiltered;

    public ExhibitionAdapter(List<ExhibitionGS> getDataAdapter, Context context){

        super();

        this.exhibitdata = getDataAdapter;

        this.exhibitListFiltered = getDataAdapter;

        this.context = context;
    }

    @Override
    public ExhibitionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_exhibition_data, parent, false);

        ExhibitionAdapter.ViewHolder viewHolder = new ExhibitionAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExhibitionAdapter.ViewHolder holder, int position) {

        ExhibitionGS exhibitionGS =  exhibitListFiltered.get(position);

        holder.txtCardExhibitionPartyName.setText(exhibitionGS.getPartyName());
        holder.txtCardExhibitionPartyPhone.setText(exhibitionGS.getPhoneNo());

        Picasso.get().load(WebName.imgurl + "exhibitionSIBPL/" + exhibitionGS.getExhbImg1()).into(holder.imgCardExhibitionImage1);
        Picasso.get().load(WebName.imgurl + "exhibitionSIBPL/" + exhibitionGS.getExhbImg2()).into(holder.imgCardExhibitionImage2);

        holder.exhbId = exhibitionGS.getExhbId();
    }

    @Override
    public int getItemCount() {
        return exhibitListFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public AppCompatImageView imgCardExhibitionImage1,imgCardExhibitionImage2;
        public TextView txtCardExhibitionPartyName,txtCardExhibitionPartyPhone;
        String exhbId;


        public ViewHolder(View itemView) {

            super(itemView);

            imgCardExhibitionImage1 = itemView.findViewById(R.id.imgCardExhibitionImage1);
            imgCardExhibitionImage2 = itemView.findViewById(R.id.imgCardExhibitionImage2);
            txtCardExhibitionPartyName = itemView.findViewById(R.id.txtCardExhibitionPartyName);
            txtCardExhibitionPartyPhone = itemView.findViewById(R.id.txtCardExhibitionPartyPhone);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    exhibitListFiltered = exhibitdata;
                } else {
                    List<ExhibitionGS> filteredList = new ArrayList<>();
                    for (ExhibitionGS row : exhibitdata) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPartyName().toLowerCase().contains(charString.toLowerCase()) || row.getPhoneNo().contains(charString)) {
                            filteredList.add(row);
                        }
                    }

                    exhibitListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = exhibitListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                exhibitListFiltered = (ArrayList<ExhibitionGS>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
