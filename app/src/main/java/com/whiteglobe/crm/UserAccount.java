package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAccount extends AppCompatActivity {

    SharedPreferences sessionUserAccount;
    private ProgressDialog pDialog;
    private static String TAG = UserAccount.class.getSimpleName();
    TextView txtUAusername,txtUAuserrole,txtUAuserstatus,txtUAdeptname,txtUAbranchname,txtUAemail,txtUAphone,txtUAaddr,txtUAzip;
    CircularImageView userImage;
    //AppCompatButton checkAppUpdate;

    DownloadManager downloadManager;
    Uri downloadURI;
    long refid;
    ArrayList<Long> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        getSupportActionBar().hide();

        sessionUserAccount = getSharedPreferences("user_details",MODE_PRIVATE);

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
        //checkAppUpdate = findViewById(R.id.checkAppUpdate);

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        makeJsonObjectRequest(sessionUserAccount.getString("uname",null));

        /*checkAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    PackageInfo pInfo = pm.getPackageInfo("com.whiteglobe.crm", 0);
                    String version = pInfo.versionName;
                    Log.d("version", "checkVersion.DEBUG: App version: "+version);

                    checkNewAppVersion(version);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });*/
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
                    Picasso.get().load(WebName.imgurl+"user_image/"+response.getString("userimage")).into(userImage);
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

    private void checkNewAppVersion(final String appversion){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading. Please Wait...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"checkappupdate.php";

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
                                downloadURI = Uri.parse(WebName.imgurl + "updt/superindia.apk");

                                final DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setAllowedOverRoaming(false);
                                request.setTitle("Super India BuildPro Downloading");
                                request.setDescription("Downloading New Version");
                                request.setVisibleInDownloadsUi(true);
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/superindia.apk");
                                refid = downloadManager.enqueue(request);
                                Log.e("OUT", "" + refid);
                                list.add(refid);



                                BroadcastReceiver onComplete = new BroadcastReceiver() {

                                    public void onReceive(Context ctxt, Intent intent) {
                                        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                                        Log.e("IN", "" + referenceId);
                                        Intent i = new Intent();
                                        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/superindia.apk");
                                        Log.d("storagedir", String.valueOf(storageDir));
                                        Uri fileURI = FileProvider.getUriForFile(UserAccount.this,BuildConfig.APPLICATION_ID + ".provider",storageDir);
                                        i.setAction(Intent.ACTION_VIEW);
                                        i.setDataAndType(fileURI, downloadManager.getMimeTypeForDownloadedFile(downloadManager.enqueue(request)));
                                        Log.d("Lofting", "About to install new .apk");
                                        startActivity(i);
                                    }
                                };

                                registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                                //Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(UserAccount.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("appversion",appversion);

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
