package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

public class Quotations extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    QuotationAdapter quotationAdapter;

    private ProgressDialog pDialog;
    private static String TAG = Quotations.class.getSimpleName();

    List<QuotationGS> allQuotations;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotations);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quotations");

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewQuotation);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allQuotations = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                QuotationGS quotationGS = allQuotations.get(position);
                Intent iQuotationDetails = new Intent(Quotations.this,QuotationDetails.class);
                iQuotationDetails.putExtra("quotno", String.valueOf(quotationGS.getQuotNo()));
                startActivity(iQuotationDetails);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllQuotations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                quotationAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                quotationAdapter.getFilter().filter(query);
                Log.d("Change",query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllQuotations() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        allQuotations.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(Quotations.this);

        String url = WebName.weburl+"getquotations.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("quotlist");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allQuotations.add(new QuotationGS(jsonObject.getString("Q_No"),jsonObject.getString("RL_Company_Name"),jsonObject.getString("Q_Date"),jsonObject.getString("RL_Title"),jsonObject.getString("Q_Amount")));
                        }

                        quotationAdapter = new QuotationAdapter(allQuotations, getApplicationContext());
                        recyclerView.setAdapter(quotationAdapter);
                        quotationAdapter.notifyDataSetChanged();
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
        getAllQuotations();
    }
}
