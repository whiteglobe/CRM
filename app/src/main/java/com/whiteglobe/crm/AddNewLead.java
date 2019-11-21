package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewLead extends AppCompatActivity {

    AppCompatEditText anlCompanyName,anlContactPersonName,anlPhoneNumber,anlLeadTitle,anlEmail,anlDepartment,anlWebsite,anlAddressStreet,anlZipcode,anlLeadStatus,anlLeadStatusDescription,anlLeadSourceDescription,anlOpportunityAmount;
    AppCompatSpinner anlCountry,anlState,anlCity,anlLeadSource;
    AppCompatButton btnANL;

    String[] leadSourceArray = new String[]{"Cold Call","Existing Customer","Self Generated","Employee","Partner","Public Relations","Direct Mail","Conference","Trade Show","Web Site","Word of mouth","Email","Campaign","Other"};
    ArrayList<String> countryName,stateName,cityName;

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = AddNewLead.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_lead);
        getSupportActionBar().hide();

        anlCompanyName = findViewById(R.id.anlCompanyName);
        anlContactPersonName = findViewById(R.id.anlContactPersonName);
        anlPhoneNumber = findViewById(R.id.anlPhoneNumber);
        anlLeadTitle = findViewById(R.id.anlLeadTitle);
        anlEmail = findViewById(R.id.anlEmail);
        anlDepartment = findViewById(R.id.anlDepartment);
        anlWebsite = findViewById(R.id.anlWebsite);
        anlAddressStreet = findViewById(R.id.anlAddressStreet);
        anlZipcode = findViewById(R.id.anlZipcode);
        anlLeadStatus = findViewById(R.id.anlLeadStatus);
        anlLeadStatusDescription = findViewById(R.id.anlLeadStatusDescription);
        anlLeadSourceDescription = findViewById(R.id.anlLeadSourceDescription);
        anlOpportunityAmount = findViewById(R.id.anlOpportunityAmount);
        anlCountry = findViewById(R.id.anlCountry);
        anlState = findViewById(R.id.anlState);
        anlCity = findViewById(R.id.anlCity);
        anlLeadSource = findViewById(R.id.anlLeadSource);
        btnANL = findViewById(R.id.btnANL);

        anlLeadStatus.setEnabled(false);

        ArrayAdapter<String> leadSourceAdapter= new ArrayAdapter<String>(AddNewLead.this,android.R.layout.simple_spinner_item, leadSourceArray);
        leadSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anlLeadSource.setAdapter(leadSourceAdapter);

        countryName = new ArrayList<>();
        stateName = new ArrayList<>();
        cityName = new ArrayList<>();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);

        getCountries();
        anlCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country=   anlCountry.getItemAtPosition(anlCountry.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
                getStates(country);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        anlState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String state=   anlState.getItemAtPosition(anlState.getSelectedItemPosition()).toString();
                //Toast.makeText(getApplicationContext(),state,Toast.LENGTH_LONG).show();
                getCities(state);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        btnANL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anlCompanyName.getText().toString().equals(""))
                {
                    anlCompanyName.findFocus();
                    anlCompanyName.setError("Please Enter Company Name.");
                }
                else if(anlPhoneNumber.getText().toString().equals(""))
                {
                    anlPhoneNumber.setError("Please Enter Phone Number.");
                }
                else if(anlAddressStreet.getText().toString().equals(""))
                {
                    anlAddressStreet.setError("Please Enter Address Street.");
                }
                else
                {
                    //Toast.makeText(getApplicationContext(),"All is well!!!",Toast.LENGTH_LONG).show();
                    adNewLead(sessionUserAccount.getString("uname",null));
                }

            }
        });
    }

    private void adNewLead(final String username){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Adding Lead...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"addregularlead.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",""+response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                finish();
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
                        Toast.makeText(AddNewLead.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("companyname",anlCompanyName.getText().toString().trim());
                params.put("contactpersonname",anlContactPersonName.getText().toString().trim());
                params.put("rlphno",anlPhoneNumber.getText().toString().trim());
                params.put("rltitle",anlLeadTitle.getText().toString().trim());
                params.put("rlemail",anlEmail.getText().toString().trim());
                params.put("department",anlDepartment.getText().toString().trim());
                params.put("rlwebsite",anlWebsite.getText().toString().trim());
                params.put("addrstreet",anlAddressStreet.getText().toString().trim());
                params.put("city",anlCity.getItemAtPosition(anlCity.getSelectedItemPosition()).toString());
                params.put("state",anlState.getItemAtPosition(anlState.getSelectedItemPosition()).toString());
                params.put("country",anlCountry.getItemAtPosition(anlCountry.getSelectedItemPosition()).toString());
                params.put("zipcode",anlZipcode.getText().toString().trim());
                params.put("status",anlLeadStatus.getText().toString().trim());
                params.put("status_description",anlLeadStatusDescription.getText().toString().trim());
                params.put("lead_source",anlLeadSource.getItemAtPosition(anlLeadSource.getSelectedItemPosition()).toString());
                params.put("lead_source_description",anlLeadSourceDescription.getText().toString().trim());
                params.put("opportunity_amount",anlOpportunityAmount.getText().toString().trim());
                params.put("username",username);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCountries() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(AddNewLead.this);

        String url = WebName.weburl+"getcountry.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("countrynames");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            countryName.add(jsonObject.getString("countryname"));
                        }

                        anlCountry.setAdapter(new ArrayAdapter<String>(AddNewLead.this, android.R.layout.simple_spinner_dropdown_item, countryName));
                    }

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

    private void getStates(String cntrName) {

        stateName.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(AddNewLead.this);

        String url = WebName.weburl+"getstate.php?country="+cntrName;
        //Log.d("URL",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("statenames");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            stateName.add(jsonObject.getString("statename"));
                        }

                        anlState.setAdapter(new ArrayAdapter<String>(AddNewLead.this, android.R.layout.simple_spinner_dropdown_item, stateName));
                    }

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

    private void getCities(String stName) {

        cityName.clear();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(AddNewLead.this);

        String url = WebName.weburl+"getcity.php?state="+stName;
        //Log.d("URL",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("citynames");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            cityName.add(jsonObject.getString("cityname"));
                        }

                        anlCity.setAdapter(new ArrayAdapter<String>(AddNewLead.this, android.R.layout.simple_spinner_dropdown_item, cityName));
                    }

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
}
