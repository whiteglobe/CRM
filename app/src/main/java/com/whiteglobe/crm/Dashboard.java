package com.whiteglobe.crm;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    FloatingActionButton userAccount,logout,leads,meetings,projects,products,customers,attendance,tasks,changepassword;
    TextView txtUserMapLocations;
    SharedPreferences sessionDashboard;
    private static final String TAG = "Dashboard";
    private ProgressDialog pDialog;
    TelephonyManager telephonyManager;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionDashboard = getSharedPreferences("user_details",MODE_PRIVATE);

        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(sessionDashboard.getString("username",null));
        //Toast.makeText(getApplicationContext(),sessionDashboard.getString("uname",null), Toast.LENGTH_SHORT).show();

        myReceiver = new MyReceiver();

        // Check that the user hasn't revoked permissions by going to Settings.
        if (!checkPermissions()) {
            requestPermissions();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return;
        }

        switchOnGPS();

        //To send userdata to server
        deviceId();

        //To go in User Account Activity
        userAccount();

        //For Logging Out User
        logout();

        //To go in Leads Activity
        leads();

        //To go in Meetings Activity
        meetings();

        //To go in Projects Activity
        projects();

        //To go in Products Activity
        products();

        //To go in Customers Activity
        customers();

        //To take User Attendance
        attendance();

        //To view users on map
        tasks();

        //To change password
        changepassword();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            try {
                mService.requestLocationUpdates();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private void deviceId() {
        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
    }

    private void firebaseToken(final String imei)
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.fcm_token, token);
                        //Log.d(TAG, msg);
                        sessionDashboard = getSharedPreferences("user_details",MODE_PRIVATE);
                        sendTokenToServer(imei,token,sessionDashboard.getString("uname",null));
                        //Toast.makeText(Dashboard.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_dashboard),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(Dashboard.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        switch (requestCode) {
            case 2:
            case 3:
            case REQUEST_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    //mService.requestLocationUpdates();
                }
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String imeiNumber = telephonyManager.getImei();
                    //Toast.makeText(Dashboard.this,imeiNumber,Toast.LENGTH_LONG).show();
                    //Log.d("IMEI",imeiNumber);
                    //Firebase Token
                    FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                    firebaseToken(imeiNumber);
                } else {
                    Toast.makeText(Dashboard.this,"Without permission we check",Toast.LENGTH_LONG).show();
                }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            //senLocationDataToServer(sessionDashboard.getString("uname",null),String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            if (location != null) {
                //Toast.makeText(Dashboard.this, Utils.getLocationText(location),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.

    }

    private void userAccount()
    {
        userAccount = findViewById(R.id.btnUserAccount);

        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUserAccount = new Intent(Dashboard.this, UserAccount.class);
                startActivity(iUserAccount);
            }
        });

    }

    private void logout()
    {
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.removeLocationUpdates();
                Intent iLogout = new Intent(Dashboard.this, MainActivity.class);
                SharedPreferences.Editor editor = sessionDashboard.edit();
                editor.clear();
                editor.commit();
                startActivity(iLogout);
                finish();
            }
        });
    }

    private void leads()
    {
        leads = findViewById(R.id.leads);

        leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iLeads = new Intent(Dashboard.this,Leads.class);
                startActivity(iLeads);
            }
        });
    }

    private void meetings()
    {
        meetings= findViewById(R.id.meetings);

        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMeetings = new Intent(Dashboard.this,Meetings.class);
                startActivity(iMeetings);
            }
        });
    }

    private void projects()
    {
        projects = findViewById(R.id.projects);

        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iProjects = new Intent(Dashboard.this,Projects.class);
                startActivity(iProjects);
            }
        });
    }

    private void products()
    {
        products = findViewById(R.id.products);

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iProducts = new Intent(Dashboard.this,Products.class);
                startActivity(iProducts);
            }
        });
    }

    private void customers()
    {
        customers = findViewById(R.id.customers);

        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iProducts = new Intent(Dashboard.this,Customers.class);
                startActivity(iProducts);
            }
        });
    }

    private void attendance()
    {
        attendance = findViewById(R.id.attendance);

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAttendance = new Intent(Dashboard.this,Attendance.class);
                startActivity(iAttendance);
            }
        });
    }

    private void tasks()
    {
        tasks = findViewById(R.id.tasks);
        txtUserMapLocations = findViewById(R.id.txtUserMapLocations);

        if(sessionDashboard.getString("uname",null).equals("superindia") || sessionDashboard.getString("uname",null).equals("allarakhavohra"))
        {
            tasks.setImageResource(R.drawable.ic_place_black_24dp);
            tasks.setBackgroundColor(getResources().getColor(R.color.green_700));
            txtUserMapLocations.setText("Map");

            tasks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iTasks = new Intent(Dashboard.this,SelectMap.class);
                    startActivity(iTasks);
                }
            });
        }
    }

    private void changepassword()
    {
        changepassword = findViewById(R.id.changepassword);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iChangePassword = new Intent(Dashboard.this,ChangePassword.class);
                startActivity(iChangePassword);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    private void sendTokenToServer(final String im,final String fbtoken,final String un){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging In. Please Wait...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"usernoti.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(MeetingDetails.this,response,Toast.LENGTH_LONG).show();
                        //Log.d("Response From Server", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {

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
                        Toast.makeText(Dashboard.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("imeinumber",im);
                params.put("firebasetoken",fbtoken);
                params.put("username",un);

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

    private void switchOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED :
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(Dashboard.this,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //open setting and switch on GPS manually
                            break;
                    }
                }
            }
        });
        //Give permission to access GPS
        //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 11);
    }
}
