package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProjectAllIssues extends AppCompatActivity {

    SharedPreferences sessionAllIssues;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = ProjectAllIssues.class.getSimpleName();

    FloatingActionButton btnAddProjectIssue;

    List<ProjectIssueGs> allProjectIssues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all_issues);
        getSupportActionBar().hide();

        btnAddProjectIssue = findViewById(R.id.btnAddProjectIssue);

        recyclerView = findViewById(R.id.recyclerViewProjectIssues);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allProjectIssues = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectIssueGs projectIssueGs = allProjectIssues.get(position);
                Intent iProjectIssueDetails = new Intent(ProjectAllIssues.this,ProjectIssueDetails.class);
                iProjectIssueDetails.putExtra("issueid", String.valueOf(projectIssueGs.getIssueId()));
                iProjectIssueDetails.putExtra("projectunique", getIntent().getStringExtra("projectunique"));
                startActivity(iProjectIssueDetails);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btnAddProjectIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAddIssue = new Intent(ProjectAllIssues.this,AddProjectIssue.class);
                iAddIssue.putExtra("projectunique", getIntent().getStringExtra("projectunique"));
                startActivity(iAddIssue);
            }
        });

        sessionAllIssues = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllProjectIssueDataOfUser(sessionAllIssues.getString("uname",null),getIntent().getStringExtra("projectunique"));
    }

    private void getAllProjectIssueDataOfUser(String u_name,String projectUnique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        allProjectIssues.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectAllIssues.this);

        String url = WebName.weburl+"getuserprojectissues.php?username="+u_name+"&projectunique="+projectUnique;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("projectissues");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allProjectIssues.add(new ProjectIssueGs(jsonObject.getString("PI_Title"),jsonObject.getString("PI_Posted_Date"),jsonObject.getString("PI_Status"),jsonObject.getInt("PI_Id")));
                        }
                        recyclerViewadapter = new ProjectIssueAdapter(allProjectIssues, getApplicationContext());
                        recyclerView.setAdapter(recyclerViewadapter);
                    }
                    else if(response.getInt("success") == 0)
                    {
                        Toast.makeText(getApplicationContext(),response.getString("msg"),Toast.LENGTH_LONG).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        sessionAllIssues = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllProjectIssueDataOfUser(sessionAllIssues.getString("uname",null),getIntent().getStringExtra("projectunique"));
    }
}
