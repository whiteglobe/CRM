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

public class ViewUserLeaves extends AppCompatActivity {

    SharedPreferences sessionViewUserLeaves;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = ViewUserLeaves.class.getSimpleName();

    List<UserLeaveGS> allUserLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_leaves);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewViewUserLeaves);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allUserLeaves = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserLeaveGS userLeaveGS = allUserLeaves.get(position);
                Intent iViewLeaveDetails = new Intent(ViewUserLeaves.this,LeaveDetails.class);
                iViewLeaveDetails.putExtra("leaveid", String.valueOf(userLeaveGS.getLeaveId()));
                startActivity(iViewLeaveDetails);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        sessionViewUserLeaves = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllLeaveDataOfUser(sessionViewUserLeaves.getString("uname",null));
    }

    private void getAllLeaveDataOfUser(String u_name) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        allUserLeaves.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(ViewUserLeaves.this);

        String url = WebName.weburl+"getuserleaves.php?username="+u_name;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("userleaves");
                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allUserLeaves.add(new UserLeaveGS(jsonObject.getInt("L_Id"),jsonObject.getString("L_Reason"),jsonObject.getString("L_LeavePostedDate"),jsonObject.getString("L_Status")));
                        }
                        recyclerViewadapter = new UserLeavesAdapter(allUserLeaves, getApplicationContext());
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
        sessionViewUserLeaves = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllLeaveDataOfUser(sessionViewUserLeaves.getString("uname",null));
    }
}
