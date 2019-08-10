package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);
        getSupportActionBar().hide();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);

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

                    Log.d("Name",response.getString("name"));
                    Log.d("RL_Phone",response.getString("RL_Phone"));
                    Log.d("RL_Title",response.getString("RL_Title"));
                    Log.d("RL_Email",response.getString("RL_Email"));
                    Log.d("RL_Department",response.getString("RL_Department"));
                    Log.d("RL_Website",response.getString("RL_Website"));
                    Log.d("address",response.getString("address"));
                    Log.d("RL_Status",response.getString("RL_Status"));
                    Log.d("RL_Status_Descr",response.getString("RL_Status_Descr"));
                    Log.d("RL_Lead_Source",response.getString("RL_Lead_Source"));
                    Log.d("RL_Lead_Source_Descr",response.getString("RL_Lead_Source_Descr"));
                    Log.d("RL_Opportunity_Amount",response.getString("RL_Opportunity_Amount"));


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
