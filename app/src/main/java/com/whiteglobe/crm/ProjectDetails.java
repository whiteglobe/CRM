package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectDetails extends AppCompatActivity {

    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private Handler handler = new Handler();
    private Runnable runnable;

    TextView txtProjectDetailsDays,txtProjectDetailsHours,txtProjectDetailsMinutes,txtProjectDetailsSeconds,txtProjectDetailsTasksPending,txtProjectDetailsTasksInProgress,txtProjectDetailsTasksCompleted,txtProjectDetailsIssuesNew,txtProjectDetailsIssuesSolved;

    private ProgressDialog pDialog;
    private static String TAG = ProjectDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        txtProjectDetailsDays = findViewById(R.id.txtProjectDetailsDays);
        txtProjectDetailsHours = findViewById(R.id.txtProjectDetailsHours);
        txtProjectDetailsMinutes = findViewById(R.id.txtProjectDetailsMinutes);
        txtProjectDetailsSeconds = findViewById(R.id.txtProjectDetailsSeconds);
        txtProjectDetailsTasksPending = findViewById(R.id.txtProjectDetailsTasksPending);
        txtProjectDetailsTasksInProgress = findViewById(R.id.txtProjectDetailsTasksInProgress);
        txtProjectDetailsTasksCompleted = findViewById(R.id.txtProjectDetailsTasksCompleted);
        txtProjectDetailsIssuesNew = findViewById(R.id.txtProjectDetailsIssuesNew);
        txtProjectDetailsIssuesSolved = findViewById(R.id.txtProjectDetailsIssuesSolved);

        String projectenddate = getIntent().getStringExtra("projectenddate")+" 23:59:59";

        countDownStart(projectenddate);
        getProjectDetails(getIntent().getStringExtra("projectunique"));
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

    private void getProjectDetails(String projectUnique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectDetails.this);

        String url = WebName.weburl+"projectdetails.php?projectunique="+projectUnique;
        //Log.d("url",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    txtProjectDetailsTasksPending.setText(response.getString("PTPT_Id"));
                    txtProjectDetailsTasksInProgress.setText(response.getString("IPPT_Id"));
                    txtProjectDetailsTasksCompleted.setText(response.getString("CTPT_Id"));
                    txtProjectDetailsIssuesNew.setText(response.getString("NIPI_Id"));
                    txtProjectDetailsIssuesSolved.setText(response.getString("SIPI_Id"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        requestQueue.add(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
