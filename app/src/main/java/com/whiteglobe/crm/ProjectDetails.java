package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectDetails extends AppCompatActivity {

    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler = new Handler();
    private Runnable runnable;

    TextView txtProjectDetailsDays,txtProjectDetailsHours,txtProjectDetailsMinutes,txtProjectDetailsSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        txtProjectDetailsDays = findViewById(R.id.txtProjectDetailsDays);
        txtProjectDetailsHours = findViewById(R.id.txtProjectDetailsHours);
        txtProjectDetailsMinutes = findViewById(R.id.txtProjectDetailsMinutes);
        txtProjectDetailsSeconds = findViewById(R.id.txtProjectDetailsSeconds);

        Log.d("Project Unique",getIntent().getStringExtra("projectunique"));
        Log.d("Project Enddate",getIntent().getStringExtra("projectenddate"));
        String projectenddate = getIntent().getStringExtra("projectenddate")+" 23:59:59";

        countDownStart(projectenddate);
    }

    private void countDownStart(final String enddate) {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(enddate);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        txtProjectDetailsDays.setText(String.format("%02d", Days));
                        txtProjectDetailsHours.setText(String.format("%02d", Hours));
                        txtProjectDetailsMinutes.setText(String.format("%02d", Minutes));
                        txtProjectDetailsSeconds.setText(String.format("%02d", Seconds));
                    } else {
                        handler.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
