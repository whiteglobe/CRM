package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MeetingDetails extends AppCompatActivity {

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();

    TextView txtMeetingLeadTitle,txtMeetingLeadDate,txtMeetingLeadTime,txtMeetingLeadDiscussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_details);

        getSupportActionBar().hide();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);

        txtMeetingLeadTitle = findViewById(R.id.txtMeetingLeadTitle);
        txtMeetingLeadDate = findViewById(R.id.txtMeetingLeadDate);
        txtMeetingLeadTime = findViewById(R.id.txtMeetingLeadTime);
        txtMeetingLeadDiscussion = findViewById(R.id.txtMeetingLeadDiscussion);

        getMeetingDetails(sessionUserAccount.getString("uname",null),getIntent().getStringExtra("meeting_id"));
    }

    private void getMeetingDetails(String u_name,String leadid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(MeetingDetails.this);

        String url = WebName.weburl+"meetingdetails.php?username="+u_name+"&meeting_id="+leadid;
        //Log.d("url",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    txtMeetingLeadTitle.setText(response.getString("MeetingForLead"));
                    txtMeetingLeadDate.setText(response.getString("MeetingDate"));
                    txtMeetingLeadTime.setText(response.getString("MeetingTime"));
                    txtMeetingLeadDiscussion.setText(response.getString("MeetingDiscussion"));

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
