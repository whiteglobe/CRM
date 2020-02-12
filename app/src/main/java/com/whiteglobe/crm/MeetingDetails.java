package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MeetingDetails extends AppCompatActivity {

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();

    TextView txtMeetingLeadTitle,txtMeetingLeadDate,txtMeetingLeadTime;
    AppCompatEditText txtMeetingLeadDiscussion;
    AppCompatButton btnUpdateMeetDetails,btnAddReminder;

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
        btnUpdateMeetDetails = findViewById(R.id.btnUpdateMeetDetails);
        btnAddReminder = findViewById(R.id.btnAddReminder);

        getMeetingDetails(sessionUserAccount.getString("uname",null),getIntent().getStringExtra("meeting_id"));

        btnUpdateMeetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMeetingDetails(getIntent().getStringExtra("meeting_id"));
            }
        });

        btnAddReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddReminder = new Intent(MeetingDetails.this,AddReminder.class);
                iAddReminder.putExtra("meetpartyname",txtMeetingLeadTitle.getText());
                startActivity(iAddReminder);
            }
        });
    }

    private void getMeetingDetails(String u_name,String leadid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(MeetingDetails.this);

        String url = WebName.weburl+"meetingdetails.php?username="+u_name+"&meeting_id="+leadid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    txtMeetingLeadTitle.setText(response.getString("MeetingForLead"));
                    txtMeetingLeadDate.setText(response.getString("MeetingDate"));
                    txtMeetingLeadTime.setText(response.getString("MeetingTime"));
                    txtMeetingLeadDiscussion.setText(response.getString("MeetingDiscussion"));
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

    private void updateMeetingDetails(final String meetingID){

        String url = WebName.weburl+"updatemeetingdetails.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                            else if(jsonObject.getInt("success") == 0)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MeetingDetails.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("meeting_id",meetingID);
                params.put("meeting_details",txtMeetingLeadDiscussion.getText().toString().trim());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
