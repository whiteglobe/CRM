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

public class ProjectAllTasks extends AppCompatActivity {

    SharedPreferences sessionProjectTasks;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = ProjectAllTasks.class.getSimpleName();

    List<ProjectTasksGS> allProjectTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all_tasks);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewProjectAllTasks);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allProjectTasks = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ProjectTasksGS projectTasksGS = allProjectTasks.get(position);
                Intent iProjectTaskDetails = new Intent(ProjectAllTasks.this,ProjectTaskDetails.class);
                iProjectTaskDetails.putExtra("taskid", String.valueOf(projectTasksGS.getTaskID()));
                iProjectTaskDetails.putExtra("projectunique", getIntent().getStringExtra("projectunique"));
                startActivity(iProjectTaskDetails);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        sessionProjectTasks = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllProjectTaskDataOfUser(sessionProjectTasks.getString("uname",null),getIntent().getStringExtra("projectunique"));
    }

    private void getAllProjectTaskDataOfUser(String u_name,String projectUnique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectAllTasks.this);

        String url = WebName.weburl+"getuserprojecttasks.php?username="+u_name+"&projectunique="+projectUnique;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("projecttasks");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allProjectTasks.add(new ProjectTasksGS(jsonObject.getString("PT_Title"),jsonObject.getString("PT_Date"),jsonObject.getInt("PT_Id")));
                        }

                        recyclerViewadapter = new ProjectTasksAdapter(allProjectTasks, getApplicationContext());
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
