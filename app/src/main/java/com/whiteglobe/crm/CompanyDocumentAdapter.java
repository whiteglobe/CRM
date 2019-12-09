package com.whiteglobe.crm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CompanyDocumentAdapter  extends RecyclerView.Adapter<CompanyDocumentAdapter.ViewHolder> {
    Context context;
    List<CompanyDocumentGS> companyDocData;

    public CompanyDocumentAdapter(List<CompanyDocumentGS> getDataAdapter, Context context){

        super();

        this.companyDocData = getDataAdapter;

        this.context = context;
    }

    @Override
    public CompanyDocumentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_company_documents, parent, false);

        CompanyDocumentAdapter.ViewHolder viewHolder = new CompanyDocumentAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CompanyDocumentAdapter.ViewHolder holder, int position) {

        CompanyDocumentGS companyDocumentGS =  companyDocData.get(position);

        holder.txtCardCompanyDocumentTitle.setText(convert(companyDocumentGS.getCompanyDocTitle()));
        holder.txtCardCompanyDocumentRemarks.setText(convert(companyDocumentGS.getCompanyDocRemarks()));
        holder.txtCardCompanyDocumentExpiryDate.setText(companyDocumentGS.getCompanyDocExpiryDate());

        holder.documentName = companyDocumentGS.getCompanyDocName();
    }

    @Override
    public int getItemCount() {
        return companyDocData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardCompanyDocumentTitle,txtCardCompanyDocumentRemarks,txtCardCompanyDocumentExpiryDate;
        String documentName;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardCompanyDocumentTitle = itemView.findViewById(R.id.txtCardCompanyDocumentTitle) ;
            txtCardCompanyDocumentRemarks = itemView.findViewById(R.id.txtCardCompanyDocumentRemarks) ;
            txtCardCompanyDocumentExpiryDate = itemView.findViewById(R.id.txtCardCompanyDocumentExpiryDate) ;
        }
    }

    static String convert(String str)
    {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }
}