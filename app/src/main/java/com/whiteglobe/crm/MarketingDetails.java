package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MarketingDetails extends AppCompatActivity {

    SharedPreferences sessionMarketingDetails;
    private ProgressDialog pDialog;
    private static String TAG = MarketingDetails.class.getSimpleName();

    TextView txtMarketingLeadName,txtMarketingLeadPhone,txtMarketingLeadEmail,txtMarketingLeadAddress,txtMarketingLeadIndustry;
    AppCompatEditText txtMarketingLeadDiscussion;
    AppCompatButton btnUpdateMarketDetails,btnAddMarketingReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_details);
        getSupportActionBar().hide();

        sessionMarketingDetails = getSharedPreferences("user_details",MODE_PRIVATE);

        txtMarketingLeadName = findViewById(R.id.txtMarketingLeadName);
        txtMarketingLeadPhone = findViewById(R.id.txtMarketingLeadPhone);
        txtMarketingLeadEmail = findViewById(R.id.txtMarketingLeadEmail);
        txtMarketingLeadAddress = findViewById(R.id.txtMarketingLeadAddress);
        txtMarketingLeadIndustry = findViewById(R.id.txtMarketingLeadIndustry);
        txtMarketingLeadDiscussion = findViewById(R.id.txtMarketingLeadDiscussion);
        btnUpdateMarketDetails = findViewById(R.id.btnUpdateMarketDetails);
        btnAddMarketingReminder = findViewById(R.id.btnAddMarketingReminder);

        getMarketingDetails(sessionMarketingDetails.getString("uname",null),getIntent().getStringExtra("marketing_id"));

        btnUpdateMarketDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMarketingDetails(getIntent().getStringExtra("marketing_id"));
            }
        });

        btnAddMarketingReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iAddReminder = new Intent(MarketingDetails.this,AddReminder.class);
                iAddReminder.putExtra("meetpartyname",txtMarketingLeadName.getText());
                startActivity(iAddReminder);
            }
        });
    }

    private void getMarketingDetails(String u_name,String marketleadid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(MarketingDetails.this);

        String url = WebName.weburl+"marketingdetails.php?username="+u_name+"&marketing_id="+marketleadid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    txtMarketingLeadName.setText(response.getString("ML_Name"));
                    txtMarketingLeadPhone.setText(response.getString("ML_Phone"));
                    txtMarketingLeadEmail.setText(response.getString("ML_Email"));
                    txtMarketingLeadAddress.setText(response.getString("ML_Address"));
                    txtMarketingLeadIndustry.setText(response.getString("ML_Industry"));
                    txtMarketingLeadDiscussion.setText(response.getString("ML_Description"));
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

    private void updateMarketingDetails(final String meetingID){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Updating Details...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"updatemarketingdetails.php";

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
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MarketingDetails.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("marketing_id",meetingID);
                params.put("discussion",txtMarketingLeadDiscussion.getText().toString().trim());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
