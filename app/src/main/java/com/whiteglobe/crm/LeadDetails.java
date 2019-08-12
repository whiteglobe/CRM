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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class LeadDetails extends AppCompatActivity {

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();

    TextView txtLeadCompanyName,txtLeadContactPerson,txtLeadPhone,txtLeadTitle,txtLeadMail,txtLeadDepartment,txtLeadWebsite,txtLeadAddress,txtLeadStatus,txtLeadStatusDescr,txtLeadSource,txtLeadSourceDescr,txtLeadOpportunityAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);
        getSupportActionBar().hide();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);

        txtLeadCompanyName = findViewById(R.id.txtLeadCompanyName);
        txtLeadContactPerson = findViewById(R.id.txtLeadContactPerson);;
        txtLeadPhone = findViewById(R.id.txtLeadPhone);;
        txtLeadTitle = findViewById(R.id.txtLeadTitle);;
        txtLeadMail = findViewById(R.id.txtLeadMail);;
        txtLeadDepartment = findViewById(R.id.txtLeadDepartment);;
        txtLeadWebsite = findViewById(R.id.txtLeadWebsite);;
        txtLeadAddress = findViewById(R.id.txtLeadAddress);;
        txtLeadStatus = findViewById(R.id.txtLeadStatus);;
        txtLeadStatusDescr = findViewById(R.id.txtLeadStatusDescr);;
        txtLeadSource = findViewById(R.id.txtLeadSource);;
        txtLeadSourceDescr = findViewById(R.id.txtLeadSourceDescr);;
        txtLeadOpportunityAmt = findViewById(R.id.txtLeadOpportunityAmt);;

        getLeadDetails(sessionUserAccount.getString("uname",null),getIntent().getStringExtra("lead_id"));
    }

    private void getLeadDetails(String u_name,String leadid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(LeadDetails.this);

        String url = WebName.weburl+"leaddetails.php?username="+u_name+"&lead_id="+leadid;
        Log.d("url",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    txtLeadCompanyName.setText(response.getString("RL_Company_Name"));
                    txtLeadContactPerson.setText(response.getString("RL_Contact_Person_Name"));
                    txtLeadPhone.setText(response.getString("RL_Phone"));
                    txtLeadTitle.setText(response.getString("RL_Title"));
                    txtLeadMail.setText(response.getString("RL_Email"));
                    txtLeadDepartment.setText(response.getString("RL_Department"));
                    txtLeadWebsite.setText(response.getString("RL_Website"));
                    txtLeadAddress.setText(response.getString("address"));
                    txtLeadStatus.setText(response.getString("RL_Status"));
                    txtLeadStatusDescr.setText(response.getString("RL_Status_Descr").trim());
                    txtLeadSource.setText(response.getString("RL_Lead_Source"));
                    txtLeadSourceDescr.setText(response.getString("RL_Lead_Source_Descr").trim());
                    txtLeadOpportunityAmt.setText(response.getString("RL_Opportunity_Amount"));


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
