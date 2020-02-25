package com.whiteglobe.crm;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
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
import com.firebase.client.Firebase;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    FloatingActionButton userAccount,logout,leads,meetings,projects,products,customers,attendance,tasks,changepassword,companydocs,chat,quotations,marketing,exhibition;
    TextView txtUserMapLocations;
    SharedPreferences sessionDashboard;
    private static final String TAG = "Dashboard";
    private ProgressDialog pDialog;
    TelephonyManager telephonyManager;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };

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

        telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&  ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        switchOnGPS();

        if(sessionDashboard.getString("uname",null).equals("superindia") || sessionDashboard.getString("uname",null).equals("allarakhavohra"))
        {
            getAllCompanyDocumentNotifications();
        }

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

        //To view company documents
        companydocs();

        //To chat with other users
        chat();

        //To view all quotations
        quotations();

        //To view all marketing leads
        marketing();

        //To enter enquiry details in exhibition
        exhibition();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        try {
            mService.requestLocationUpdates();
        }catch (Exception e){
            e.printStackTrace();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String imeiNumber = telephonyManager.getImei();
                    //Firebase Token
                    FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                    firebaseToken(imeiNumber);
                    for (Intent intent : POWERMANAGER_INTENTS) {
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            startActivity(intent);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(Dashboard.this,"Without permission we check",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

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
                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                String ip;
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                        sendUserLogToServer(sessionDashboard.getString("uname",null),ip);
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        ip = getIPAddress(true);
                        sendUserLogToServer(sessionDashboard.getString("uname",null),ip);
                    }
                }
                Intent iLogout = new Intent(Dashboard.this, MainActivity.class);
                SharedPreferences.Editor editor = sessionDashboard.edit();
                editor.clear();
                editor.commit();
                startActivity(iLogout);
                finish();
            }
        });
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
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
                mService.requestLocationUpdates();
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

    private void companydocs()
    {
        companydocs = findViewById(R.id.companydocs);

        companydocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iChangePassword = new Intent(Dashboard.this,CompanyDocuments.class);
                startActivity(iChangePassword);
            }
        });
    }

    private void quotations()
    {
        quotations = findViewById(R.id.quotations);

        quotations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iQuotation = new Intent(Dashboard.this,Quotations.class);
                startActivity(iQuotation);
            }
        });
    }

    private void marketing()
    {
        marketing = findViewById(R.id.marketing);

        marketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMarketing = new Intent(Dashboard.this,Marketing.class);
                startActivity(iMarketing);
            }
        });
    }

    private void exhibition()
    {
        exhibition = findViewById(R.id.exhibition);

        exhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iExhibition = new Intent(Dashboard.this,Exhibition.class);
                startActivity(iExhibition);
            }
        });
    }

    private void chat()
    {
        chat = findViewById(R.id.chat);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://superindiabuildpro-8b6bd.firebaseio.com/users.json";
                final ProgressDialog pd = new ProgressDialog(Dashboard.this);
                pd.setMessage("Loading...");
                pd.show();

                final String user = sessionDashboard.getString("uname",null);
                final String pass = "Whiteglobe@123";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("null")){
                            Toast.makeText(Dashboard.this, "user not found to chat", Toast.LENGTH_LONG).show();
                        }
                        else{
                            try {
                                JSONObject obj = new JSONObject(s);

                                if(!obj.has(user)){
                                    Toast.makeText(Dashboard.this, "user not found", Toast.LENGTH_LONG).show();
                                }
                                else if(obj.getJSONObject(user).getString("password").equals(pass)){
                                    UserDetails.username = user;
                                    UserDetails.password = pass;
                                    Intent iChangePassword = new Intent(Dashboard.this,ChatUsers.class);
                                    startActivity(iChangePassword);
                                }
                                else {
                                    Toast.makeText(Dashboard.this, "incorrect password", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        pd.dismiss();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(Dashboard.this);
                rQueue.add(request);
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

    private void sendUserLogToServer(final String usrnm,final String ipaddr){

        String url = WebName.weburl+"userlogs.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("username",usrnm);
                params.put("ipaddr",ipaddr);

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

    private void generateNotification(String msg){
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(Dashboard.this,CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Super India BuildPro")
                        .setContentText(msg);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);
        notificationManager.notify(455, mBuilder.build());
    }

    private void getAllCompanyDocumentNotifications() {

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        String url = WebName.weburl+"getdocumentnotifications.php";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("docsnoti");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            generateNotification(jsonObject.getString("notifics"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        // Adding request to request queue
        requestQueue.add(jsonObjReq);
    }
}
