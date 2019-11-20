package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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

public class LeaveDetails extends AppCompatActivity {

    TextView txtLeaveFromDate,txtLeaveToDate,txtLeaveReason,txtLeaveStatus,txtLeavePostedDate,txtLeaveApprovedBy,txtReasonOfApproval,txtLeaveLeaveApprovedDate;

    private ProgressDialog pDialog;
    private static String TAG = LeaveDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);
        getSupportActionBar().hide();

        txtLeaveFromDate = findViewById(R.id.txtLeaveFromDate);
        txtLeaveToDate = findViewById(R.id.txtLeaveToDate);
        txtLeaveReason = findViewById(R.id.txtLeaveReason);
        txtLeaveStatus = findViewById(R.id.txtLeaveStatus);
        txtLeavePostedDate = findViewById(R.id.txtLeavePostedDate);
        txtLeaveApprovedBy = findViewById(R.id.txtLeaveApprovedBy);
        txtReasonOfApproval = findViewById(R.id.txtReasonOfApproval);
        txtLeaveLeaveApprovedDate = findViewById(R.id.txtLeaveLeaveApprovedDate);

        getProjectIssueDetails(Integer.valueOf(getIntent().getStringExtra("leaveid")));
    }

    private void getProjectIssueDetails(int leaveid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveDetails.this);

        String url = WebName.weburl+"userleavedetails.php?leaveid="+leaveid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    txtLeaveFromDate.setText(response.getString("L_FromDate"));
                    txtLeaveToDate.setText(response.getString("L_ToDate"));
                    txtLeaveReason.setText(response.getString("L_Reason"));
                    txtLeavePostedDate.setText(response.getString("L_LeavePostedDate"));
                    if(response.getString("L_Status").equals("Approved"))
                    {
                        txtLeaveStatus.setTextColor(Color.GREEN);
                    }
                    else if(response.getString("L_Status").equals("Unapproved"))
                    {
                        txtLeaveStatus.setTextColor(Color.RED);
                    }
                    else if(response.getString("L_Status").equals("Pending"))
                    {
                        txtLeaveStatus.setTextColor(Color.rgb(255,69,0));
                    }
                    txtLeaveStatus.setText(response.getString("L_Status"));
                    txtLeaveApprovedBy.setText(response.getString("L_ApprovedBy"));
                    txtReasonOfApproval.setText(response.getString("L_ApproveReason"));
                    txtLeaveLeaveApprovedDate.setText(response.getString("L_ApprovedDate"));
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
