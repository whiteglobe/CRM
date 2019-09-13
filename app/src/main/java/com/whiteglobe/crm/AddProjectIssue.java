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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddProjectIssue extends AppCompatActivity {

    AppCompatEditText edtProjectIssueTitle,edtProjectIssueDetails;
    AppCompatButton btnAddProjectIssue;

    private ProgressDialog pDialog;
    SharedPreferences sessionAddIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_issue);
        getSupportActionBar().hide();

        edtProjectIssueTitle = findViewById(R.id.edtProjectIssueTitle);
        edtProjectIssueDetails = findViewById(R.id.edtProjectIssueDetails);
        btnAddProjectIssue = findViewById(R.id.btnAddProjectIssue);

        sessionAddIssues = getSharedPreferences("user_details",MODE_PRIVATE);

        btnAddProjectIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projUniq = getIntent().getStringExtra("projectunique");

                addIssue(sessionAddIssues.getString("uname",null),edtProjectIssueTitle.getText().toString().trim(),edtProjectIssueDetails.getText().toString().trim(),projUniq);
            }
        });
    }

    private void addIssue(final String username,final String issueTitle, final String issueDetails,final String projectUnique){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"addprojectissue.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                //Intent iAllIssue = new Intent(AddProjectIssue.this,ProjectAllIssues.class);
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
                        Toast.makeText(AddProjectIssue.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("issueTitle",issueTitle);
                params.put("issueDetails",issueDetails);
                params.put("projectUnique",projectUnique);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
