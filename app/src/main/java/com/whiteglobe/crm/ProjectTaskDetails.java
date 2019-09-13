package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class ProjectTaskDetails extends AppCompatActivity {

    SharedPreferences sessionProjectTaskDetails;
    private ProgressDialog pDialog;
    private static String TAG = ProjectTaskDetails.class.getSimpleName();

    TextView txtProjectTaskTitle,txtProjectTaskDetails,txtProjectTaskDate,txtProjectTaskStatus;
    AppCompatSpinner spnProjectTaskStatus;
    AppCompatButton btnChangeProjectTaskStatus,btnUpdateProjectTaskStatus;

    String[] taskStatusArray = new String[]{"Pending","In Progress","Completed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task_details);
        getSupportActionBar().hide();

        txtProjectTaskTitle = findViewById(R.id.txtProjectTaskTitle);
        txtProjectTaskDetails = findViewById(R.id.txtProjectTaskDetails);
        txtProjectTaskDate = findViewById(R.id.txtProjectTaskDate);
        txtProjectTaskStatus = findViewById(R.id.txtProjectTaskStatus);
        spnProjectTaskStatus = findViewById(R.id.spnProjectTaskStatus);
        btnChangeProjectTaskStatus = findViewById(R.id.btnChangeProjectTaskStatus);
        btnUpdateProjectTaskStatus =findViewById(R.id.btnUpdateProjectTaskStatus);

        sessionProjectTaskDetails = getSharedPreferences("user_details",MODE_PRIVATE);

        spnProjectTaskStatus.setVisibility(View.GONE);
        btnUpdateProjectTaskStatus.setVisibility(View.GONE);
        btnChangeProjectTaskStatus.setVisibility(View.GONE);

        ArrayAdapter<String> projectTaskAdapter= new ArrayAdapter<String>(ProjectTaskDetails.this,android.R.layout.simple_spinner_item, taskStatusArray);
        projectTaskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnProjectTaskStatus.setAdapter(projectTaskAdapter);

        getProjectTaskDetails(sessionProjectTaskDetails.getString("uname",null),Integer.parseInt(getIntent().getStringExtra("taskid")),getIntent().getStringExtra("projectunique"));

        btnChangeProjectTaskStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnProjectTaskStatus.setVisibility(View.VISIBLE);
                btnUpdateProjectTaskStatus.setVisibility(View.VISIBLE);
                txtProjectTaskStatus.setVisibility(View.GONE);
                btnChangeProjectTaskStatus.setVisibility(View.GONE);
            }
        });

        btnUpdateProjectTaskStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getProjectTaskDetails(String u_name,int taskid,String projectunique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectTaskDetails.this);

        String url = WebName.weburl+"projecttaskdetails.php?username="+u_name+"&taskid="+taskid+"&projectunique="+projectunique;
        //Log.d("url",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG, response.toString());
                try {
                    txtProjectTaskTitle.setText(response.getString("PT_Title"));
                    txtProjectTaskDetails.setText(response.getString("PT_Descr"));
                    txtProjectTaskDate.setText(response.getString("PT_Date"));
                    if(response.getString("PT_Status").equals("Completed"))
                    {
                        txtProjectTaskStatus.setTextColor(Color.GREEN);
                    }
                    else if(response.getString("PT_Status").equals("Pending"))
                    {
                        txtProjectTaskStatus.setTextColor(Color.RED);
                    }
                    else if(response.getString("PT_Status").equals("In Progress"))
                    {
                        txtProjectTaskStatus.setTextColor(Color.rgb(204,204,0));
                    }
                    txtProjectTaskStatus.setText(response.getString("PT_Status"));
                    btnChangeProjectTaskStatus.setVisibility(View.VISIBLE);
                    if(response.getString("PT_Status").equals("Completed"))
                    {
                        btnChangeProjectTaskStatus.setVisibility(View.GONE);
                        btnUpdateProjectTaskStatus.setVisibility(View.GONE);
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
