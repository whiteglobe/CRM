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

public class ProjectAllImages extends AppCompatActivity {

    SharedPreferences sessionAllImages;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = ProjectAllIssues.class.getSimpleName();

    FloatingActionButton btnAddProjectImage;

    List<ProjectImageGS> allProjectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_all_images);
        getSupportActionBar().hide();

        btnAddProjectImage = findViewById(R.id.btnAddProjectImage);

        recyclerView = findViewById(R.id.recyclerViewProjectImages);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allProjectImage = new ArrayList<>();

        btnAddProjectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAddImage = new Intent(ProjectAllImages.this,AddProjectImage.class);
                iAddImage.putExtra("projectunique", getIntent().getStringExtra("projectunique"));
                startActivity(iAddImage);
            }
        });

        sessionAllImages = getSharedPreferences("user_details",MODE_PRIVATE);
        //sessionAllImages.getString("uname",null)
        getAllProjectIssueDataOfUser(getIntent().getStringExtra("projectunique"));
    }

    private void getAllProjectIssueDataOfUser(final String projectUnique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        allProjectImage.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectAllImages.this);

        String url = WebName.weburl+"getuserprojectimages.php?projectunique="+projectUnique;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("projectimages");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allProjectImage.add(new ProjectImageGS(jsonObject.getString("PIM_Image"),jsonObject.getString("PIM_Remarks"),jsonObject.getString("PIM_Upload_Date"),projectUnique));
                        }
                        recyclerViewadapter = new ProjectImagesAdapter(allProjectImage, getApplicationContext());
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
        sessionAllImages = getSharedPreferences("user_details",MODE_PRIVATE);
        //sessionAllImages.getString("uname",null)
        getAllProjectIssueDataOfUser(getIntent().getStringExtra("projectunique"));
    }
}
