package com.whiteglobe.crm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Attendance extends AppCompatActivity implements LocationListener {

    TextView txtAttendance;
    AppCompatButton btnAttedannce,btnApplyForLeave;

    SharedPreferences sessionAttendance;
    private ProgressDialog pDialog;
    private static String TAG = Attendance.class.getSimpleName();

    private LocationManager locationManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getSupportActionBar().hide();

        txtAttendance = findViewById(R.id.txtAttendance);
        btnAttedannce = findViewById(R.id.btnAttedannce);
        btnApplyForLeave = findViewById(R.id.btnApplyForLeave);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());

        txtAttendance.setText("By clicking below button you will make yourself present on " + currentDate);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        sessionAttendance = getSharedPreferences("user_details",MODE_PRIVATE);
        getUserAttendance(sessionAttendance.getString("uname",null));

        btnApplyForLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iApplyForLeave = new Intent(Attendance.this,ApplyForLeave.class);
                startActivity(iApplyForLeave);
            }
        });

        btnAttedannce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void makeAttendance(final String username,final String latitude,final String longitude,final String datetime){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Marking Your Attendance...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"markattendance.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                btnAttedannce.setBackgroundColor(getResources().getColor(R.color.green_900));
                                btnAttedannce.setEnabled(false);
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
                        Toast.makeText(Attendance.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("datetime",datetime);

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

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void getUserAttendance(final String username) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        String url = WebName.weburl+"getattendancedate.php?username="+username;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        btnAttedannce.setBackgroundColor(getResources().getColor(R.color.green_900));
                        btnAttedannce.setEnabled(false);
                    }
                    else if(response.getInt("success") == 0)
                    {
                        btnAttedannce.setEnabled(true);
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

    private void showCustomDialogError(String msg) {
        final Dialog dialogError = new Dialog(this);
        dialogError.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialogError.setContentView(R.layout.dialog_error);
        dialogError.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogError.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialogError.findViewById(R.id.bt_closeDE)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.dismiss();
            }
        });
        TextView txtDEmsg = dialogError.findViewById(R.id.txtDEmsg);
        txtDEmsg.setText(msg);
        dialogError.setCanceledOnTouchOutside(false);
        dialogError.show();
        dialogError.getWindow().setAttributes(lp);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isGPSEnabled(Attendance.this))
        {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            final String currentDateandTime = sdf1.format(new Date());
            if (location != null) {
                Toast.makeText(getApplicationContext(),"Marked",Toast.LENGTH_LONG).show();
                makeAttendance(sessionAttendance.getString("uname",null),String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),currentDateandTime);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Not Marked",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            showCustomDialogError("Please enable GPS to maek your attendance");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
