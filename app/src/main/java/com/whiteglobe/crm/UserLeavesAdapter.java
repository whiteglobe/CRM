package com.whiteglobe.crm;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserLeavesAdapter extends RecyclerView.Adapter<UserLeavesAdapter.ViewHolder> {
    Context context;
    List<UserLeaveGS> userleavedata;

    public UserLeavesAdapter(List<UserLeaveGS> getDataAdapter, Context context){

        super();

        this.userleavedata = getDataAdapter;

        this.context = context;
    }

    @Override
    public UserLeavesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_view_user_leaves, parent, false);

        UserLeavesAdapter.ViewHolder viewHolder = new UserLeavesAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserLeavesAdapter.ViewHolder holder, int position) {

        UserLeaveGS getUserLeaveData =  userleavedata.get(position);

        holder.txtCardUserLeaveReason.setText(getUserLeaveData.getLeaveReason());
        holder.txtCardLeavePostedDate.setText(getUserLeaveData.getLeavePostedDate());
        if(getUserLeaveData.getLeaveStatus().equals("Approved"))
        {
            holder.txtCardLeaveStatus.setTextColor(Color.GREEN);
        }
        else if(getUserLeaveData.getLeaveStatus().equals("Unapproved"))
        {
            holder.txtCardLeaveStatus.setTextColor(Color.RED);
        }
        else if(getUserLeaveData.getLeaveStatus().equals("Pending"))
        {
            holder.txtCardLeaveStatus.setTextColor(Color.rgb(255,69,0));
        }
        holder.txtCardLeaveStatus.setText(getUserLeaveData.getLeaveStatus());
        holder.txtLeaveId = getUserLeaveData.getLeaveId();
    }

    @Override
    public int getItemCount() {

        return userleavedata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtCardUserLeaveReason,txtCardLeavePostedDate,txtCardLeaveStatus;
        public int txtLeaveId;


        public ViewHolder(View itemView) {

            super(itemView);

            txtCardUserLeaveReason = itemView.findViewById(R.id.txtCardUserLeaveReason) ;
            txtCardLeavePostedDate = itemView.findViewById(R.id.txtCardLeavePostedDate) ;
            txtCardLeaveStatus = itemView.findViewById(R.id.txtCardLeaveStatus) ;
        }
    }
}