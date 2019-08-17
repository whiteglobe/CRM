package com.whiteglobe.crm;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {
    Context context;
    List<MeetingGS> meetingdata;

    public MeetingsAdapter(List<MeetingGS> getDataAdapter, Context context){

        super();

        this.meetingdata = getDataAdapter;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_all_meetings, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MeetingGS getMeetingData =  meetingdata.get(position);

        holder.meetimgTitle.setText(getMeetingData.getMeetingTitle());
        holder.meetingDate.setText(getMeetingData.getMeetingDate());
        holder.meetingTime.setText(getMeetingData.getMeetingTime());
        holder.leadID = getMeetingData.getMeetingId();
    }

    @Override
    public int getItemCount() {

        return meetingdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView meetimgTitle,meetingDate,meetingTime;
        public int leadID;


        public ViewHolder(View itemView) {

            super(itemView);

            meetimgTitle = itemView.findViewById(R.id.txtMeetingTitle) ;
            meetingDate = itemView.findViewById(R.id.txtMeetingDate) ;
            meetingTime = itemView.findViewById(R.id.txtMeetingTime) ;
        }
    }
}
