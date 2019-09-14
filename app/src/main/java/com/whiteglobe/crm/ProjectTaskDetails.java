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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectTaskDetails extends AppCompatActivity {

    SharedPreferences sessionProjectTaskDetails;
    private ProgressDialog pDialog;
    private static String TAG = ProjectTaskDetails.class.getSimpleName();

    TextView txtProjectTaskTitle,txtProjectTaskDetails,txtProjectTaskDate,txtProjectTaskStatus;
    AppCompatSpinner spnProjectTaskStatus;
    AppCompatButton btnChangeProjectTaskStatus,btnUpdateProjectTaskStatus;

    String[] taskStatusArray = new String[]{"Select Task Status","In Progress","Completed"};

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
                if(spnProjectTaskStatus.getSelectedItem().equals("Select Task Status")) {
                    Toast.makeText(getApplicationContext(),"Please Select Appropriate Task Status To Update.",Toast.LENGTH_LONG).show();
                }
                else {
                    updateTaskStatus(Integer.parseInt(getIntent().getStringExtra("taskid")),spnProjectTaskStatus.getSelectedItem().toString());
                }
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
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

    private void updateTaskStatus(final int taskId,final String taskStatus){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"updatetaskstatus.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response from server",response);
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
                        Toast.makeText(ProjectTaskDetails.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("taskId",String.valueOf(taskId));
                params.put("taskStatus",taskStatus);

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
