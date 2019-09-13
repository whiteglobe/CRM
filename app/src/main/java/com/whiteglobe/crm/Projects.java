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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Projects extends AppCompatActivity {

    SharedPreferences sessionProjects;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = Projects.class.getSimpleName();

    List<ProjectGS> allProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewProjects);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allProjects = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectGS projectGS = allProjects.get(position);
                Intent iProjectDetails = new Intent(Projects.this,ProjectDetails.class);
                iProjectDetails.putExtra("projectunique", projectGS.getProjectUnique());
                iProjectDetails.putExtra("projectenddate", projectGS.getProjectEndDate());
                startActivity(iProjectDetails);
                //Toast.makeText(getApplicationContext(), leads.getLeadID() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        sessionProjects = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllProjectDataOfUser(sessionProjects.getString("uname",null));
    }

    private void getAllProjectDataOfUser(String u_name) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(Projects.this);

        String url = WebName.weburl+"getuserprojects.php?username="+u_name;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("projects");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allProjects.add(new ProjectGS(jsonObject.getString("P_Unique_Code"),jsonObject.getString("P_Name"),jsonObject.getString("P_Overview"),jsonObject.getString("P_Site_Clearance_Date"),jsonObject.getString("P_Start_Date"),jsonObject.getString("P_Estimated_End_Date"),jsonObject.getString("P_Status")));
                        }
                        recyclerViewadapter = new ProjectsAdapter(allProjects, getApplicationContext());
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
}
