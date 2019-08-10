package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Leads extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    View ChildView ;

    int GetItemPosition ;

    List<LeadGS> allLeads;

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewLeads);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allLeads = new ArrayList<>();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);
        getAllLeadsDataOfUser(sessionUserAccount.getString("uname",null));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                LeadGS movie = allLeads.get(position);
                Intent iLeadDetails = new Intent(Leads.this,LeadDetails.class);
                iLeadDetails.putExtra("lead_id", String.valueOf(movie.getLeadID()));
                startActivity(iLeadDetails);
                //Toast.makeText(getApplicationContext(), movie.getLeadID() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getAllLeadsDataOfUser(String u_name) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(Leads.this);

        String url = WebName.weburl+"getuserleads.php?username="+u_name;

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
                        JSONArray jsonArray = response.getJSONArray("leads");
                        //Log.d("array",jsonArray.toString());

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allLeads.add(new LeadGS(jsonObject.getInt("RL_Id"),jsonObject.getString("RL_Title"),jsonObject.getString("name"),jsonObject.getString("RL_Phone")));
                            Log.d("Phone", jsonObject.getString("RL_Phone"));
                        }


                        Log.d("Length",String.valueOf(allLeads.size()));

                        recyclerViewadapter = new LeadsAdapter(allLeads, getApplicationContext());
                        recyclerView.setAdapter(recyclerViewadapter);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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
