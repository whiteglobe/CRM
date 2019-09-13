package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ProjectIssueDetails extends AppCompatActivity {

    TextView txtProjectIssueTitle,txtProjectIssueDetails,txtProjectIssueSolution,txtProjectIssueDate,txtProjectIssueStatus;

    private ProgressDialog pDialog;
    private static String TAG = ProjectIssueDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_issue_details);
        getSupportActionBar().hide();

        txtProjectIssueTitle = findViewById(R.id.txtProjectIssueTitle);
        txtProjectIssueDetails = findViewById(R.id.txtProjectIssueDetails);
        txtProjectIssueSolution = findViewById(R.id.txtProjectIssueSolution);
        txtProjectIssueDate = findViewById(R.id.txtProjectIssueDate);
        txtProjectIssueStatus = findViewById(R.id.txtProjectIssueStatus);

        getProjectIssueDetails(Integer.parseInt(getIntent().getStringExtra("issueid")),getIntent().getStringExtra("projectunique"));
    }

    private void getProjectIssueDetails(int issueid,String projectunique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectIssueDetails.this);

        String url = WebName.weburl+"projectissuedetails.php?issueid="+issueid+"&projectunique="+projectunique;
        //Log.d("url",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    txtProjectIssueTitle.setText(response.getString("PI_Title"));
                    txtProjectIssueDetails.setText(response.getString("PI_Descr"));
                    txtProjectIssueSolution.setText(response.getString("PI_Solution"));
                    txtProjectIssueDate.setText(response.getString("PI_Posted_Date"));
                    if(response.getString("PI_Status").equals("Solved"))
                    {
                        txtProjectIssueStatus.setTextColor(Color.GREEN);
                    }
                    else if(response.getString("PI_Status").equals("New"))
                    {
                        txtProjectIssueStatus.setTextColor(Color.RED);
                    }
                    txtProjectIssueStatus.setText(response.getString("PI_Status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
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
