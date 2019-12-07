package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

public class SelectUserOnMap extends AppCompatActivity {

    AppCompatSpinner spnSelectUser;
    DatePicker selectDate;
    AppCompatButton btnShowUser;
    ArrayList<String> userList;

    private ProgressDialog pDialog;
    private static String TAG = SelectUserOnMap.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_on_map);
        getSupportActionBar().hide();

        userList = new ArrayList<>();

        spnSelectUser = findViewById(R.id.spnSelectUser);
        selectDate = findViewById(R.id.selectDate);
        btnShowUser = findViewById(R.id.btnShowUser);

        btnShowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userForMap = spnSelectUser.getItemAtPosition(spnSelectUser.getSelectedItemPosition()).toString();
                int day = selectDate.getDayOfMonth();
                int month = selectDate.getMonth() + 1;
                int year = selectDate.getYear();
                String dateOfUser = year + "-" + month + "-" + String.format("%02d",day);

                String url = WebName.weburl + "getuseronmap.php?username="+userForMap+"&seldate="+dateOfUser;
                Intent iUserOnMap = new Intent(SelectUserOnMap.this,UsersOnMap.class);
                iUserOnMap.putExtra("url",url);
                startActivity(iUserOnMap);
            }
        });

        getUsers();
    }

    private void getUsers() {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Getting User List...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(SelectUserOnMap.this);

        String url = WebName.weburl+"getuserlist.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("usernames");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            userList.add(jsonObject.getString("username"));
                        }

                        spnSelectUser.setAdapter(new ArrayAdapter<String>(SelectUserOnMap.this, android.R.layout.simple_spinner_dropdown_item, userList));
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
