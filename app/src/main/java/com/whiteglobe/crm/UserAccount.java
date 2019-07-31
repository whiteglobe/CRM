package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAccount extends AppCompatActivity {

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = MainActivity.class.getSimpleName();
    TextView txtUAusername,txtUAuserrole,txtUAuserstatus,txtUAdeptname,txtUAbranchname,txtUAemail,txtUAphone,txtUAaddr,txtUAzip;
    CircularImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        getSupportActionBar().hide();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),sessionUserAccount.getString("uname",null),Toast.LENGTH_LONG).show();

        txtUAusername = findViewById(R.id.txtUAusername);
        txtUAuserrole = findViewById(R.id.txtUAuserrole);
        txtUAuserstatus = findViewById(R.id.txtUAuserstatus);
        txtUAdeptname = findViewById(R.id.txtUAdeptname);
        txtUAbranchname = findViewById(R.id.txtUAbranchname);
        txtUAemail = findViewById(R.id.txtUAemail);
        txtUAphone = findViewById(R.id.txtUAphone);
        txtUAaddr = findViewById(R.id.txtUAaddr);
        txtUAzip = findViewById(R.id.txtUAzip);
        userImage = findViewById(R.id.imgUAuserimage);

        FloatingActionButton btnUpdateUser = findViewById(R.id.btnEditUser);

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUpdateUser = new Intent(UserAccount.this,UpdateUser.class);
                startActivity(iUpdateUser);
            }
        });

        makeJsonObjectRequest(sessionUserAccount.getString("uname",null));
    }

    private void makeJsonObjectRequest(String u_name) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(UserAccount.this);

        String url = WebName.weburl+"get_userdata.php?u_name="+u_name;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
               url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object
                    txtUAusername.setText(response.getString("userfullname"));
                    txtUAuserrole.setText(response.getString("userdesig"));
                    txtUAuserstatus.setText(response.getString("userstatus"));
                    txtUAdeptname.setText(response.getString("userdept"));
                    txtUAbranchname.setText(response.getString("userbranch"));
                    txtUAemail.setText(response.getString("useremail"));
                    txtUAphone.setText(response.getString("userphone"));
                    txtUAaddr.setText(response.getString("useraddr"));
                    txtUAzip.setText(response.getString("userzip"));
                    Picasso.get().load(response.getString("userimage")).into(userImage);


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
