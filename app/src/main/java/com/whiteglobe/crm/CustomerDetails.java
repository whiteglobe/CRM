package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class CustomerDetails extends AppCompatActivity {

    TextView txtCustomerName,txtContactPersonName,txtCustomerPhone,txtCustomerMail,txtCustomerRegistrationType,txtCustomerGSTNo,txtCustomerWebsite,txtCustomerAddress,txtCustomerDescription,txtCustomerIndustry;

    private ProgressDialog pDialog;
    private static String TAG = CustomerDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        getSupportActionBar().hide();

        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtContactPersonName = findViewById(R.id.txtContactPersonName);
        txtCustomerPhone = findViewById(R.id.txtCustomerPhone);
        txtCustomerMail = findViewById(R.id.txtCustomerMail);
        txtCustomerRegistrationType = findViewById(R.id.txtCustomerRegistrationType);
        txtCustomerGSTNo = findViewById(R.id.txtCustomerGSTNo);
        txtCustomerWebsite = findViewById(R.id.txtCustomerWebsite);
        txtCustomerAddress = findViewById(R.id.txtCustomerAddress);
        txtCustomerDescription = findViewById(R.id.txtCustomerDescription);
        txtCustomerIndustry = findViewById(R.id.txtCustomerIndustry);

        getCustomerDetails(getIntent().getStringExtra("custid"));
    }

    private void getCustomerDetails(String custid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerDetails.this);

        String url = WebName.weburl+"customerdetails.php?custid="+custid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object

                    txtCustomerName.setText(response.getString("CustomerName"));
                    txtContactPersonName.setText(response.getString("ContactPersonName"));
                    txtCustomerPhone.setText(response.getString("CustomerOfficePhone"));
                    txtCustomerMail.setText(response.getString("CustomerEmail"));
                    txtCustomerRegistrationType.setText(response.getString("CustomerRegistrationType"));
                    txtCustomerGSTNo.setText(response.getString("CustomerGST").toUpperCase());
                    txtCustomerWebsite.setText(response.getString("CustomerWebsite"));
                    txtCustomerAddress.setText(response.getString("address"));
                    txtCustomerDescription.setText(response.getString("CustomerDescription").trim());
                    txtCustomerIndustry.setText(response.getString("CustomerIndustry"));


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
