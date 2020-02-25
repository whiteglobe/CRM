package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

public class Exhibition extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ExhibitionAdapter exhibitionAdapter;

    FloatingActionButton btnAddNewExhibition;
    private ProgressDialog pDialog;
    private static String TAG = Exhibition.class.getSimpleName();

    List<ExhibitionGS> allexhibition;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);

        Toolbar toolbar = findViewById(R.id.toolbarExhibition);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Exhibition");

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnAddNewExhibition = findViewById(R.id.btnAddNewExhibition);

        btnAddNewExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iExhb = new Intent(Exhibition.this,AddExhibition.class);
                startActivity(iExhb);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewExhibition);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allexhibition = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ExhibitionGS exhibitionGS = allexhibition.get(position);
                Intent iLeadDetails = new Intent(Exhibition.this,ExhibitionDetails.class);
                iLeadDetails.putExtra("exhb_id", exhibitionGS.getExhbId());
                startActivity(iLeadDetails);
                //Toast.makeText(getApplicationContext(), meetings.getMeetingId() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllExhibitionData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exhibition, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_exhibition).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                exhibitionAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                exhibitionAdapter.getFilter().filter(query);
                Log.d("Change",query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void getAllExhibitionData() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        allexhibition.clear();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(Exhibition.this);

        String url = WebName.weburl+"getexhibitiondata.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("exhibitions");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allexhibition.add(new ExhibitionGS(jsonObject.getString("Exhb_ID"),jsonObject.getString("Exhb_Image1"),jsonObject.getString("Exhb_Image2"),jsonObject.getString("Exhb_PartyName"),jsonObject.getString("Exhb_PhoneNo")));
                        }
                        exhibitionAdapter = new ExhibitionAdapter(allexhibition, getApplicationContext());
                        recyclerView.setAdapter(exhibitionAdapter);
                        exhibitionAdapter.notifyDataSetChanged();
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
        getAllExhibitionData();
    }
}
