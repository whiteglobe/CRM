package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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

public class ProjectDocuments extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    private ProgressDialog pDialog;
    private static String TAG = ProjectDocuments.class.getSimpleName();

    List<ProjectDocumentsGS> allProjectDocuments;

    DownloadManager downloadManager;
    Uri downloadURI;
    long refid;
    ArrayList<Long> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_documents);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerViewProjectDocuments);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        allProjectDocuments = new ArrayList<>();

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                list.clear();
                final ProjectDocumentsGS projectDocumentsGS = allProjectDocuments.get(position);
                String mUrl = WebName.imgurl + "project_files/" + getIntent().getStringExtra("projectunique") + "/" + projectDocumentsGS.getDocName();
                downloadURI = Uri.parse(mUrl);

                DownloadManager.Request request = new DownloadManager.Request(downloadURI);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle("Super India BuildPro Downloading");
                request.setDescription("Downloading " + projectDocumentsGS.getDocName());
                request.setVisibleInDownloadsUi(true);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/"  + projectDocumentsGS.getDocName());
                refid = downloadManager.enqueue(request);
                //Log.e("OUT", "" + refid);
                list.add(refid);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        getAllProjectDocumentsOfUser(getIntent().getStringExtra("projectunique"));
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.e("IN", "" + referenceId);

            list.remove(referenceId);

            if (list.isEmpty())
            {
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ProjectDocuments.this,CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Super India BuildPro")
                                .setContentText("File Download Completed.");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(mChannel);
                notificationManager.notify(455, mBuilder.build());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }

    private void getAllProjectDocumentsOfUser(String projectUnique) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        allProjectDocuments.clear();

        RequestQueue requestQueue = Volley.newRequestQueue(ProjectDocuments.this);

        String url = WebName.weburl+"getuserprojectdocuments.php?projectunique="+projectUnique;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    // Parsing json object response
                    // response will be a json object
                    if(response.getInt("success") == 1)
                    {
                        JSONArray jsonArray = response.getJSONArray("projectdocs");

                        JSONObject jsonObject;

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            jsonObject = jsonArray.getJSONObject(i);
                            allProjectDocuments.add(new ProjectDocumentsGS(jsonObject.getInt("PD_Id"),jsonObject.getString("PD_Name"),jsonObject.getString("PD_Uploaded_Date"),jsonObject.getString("PD_Uploaded_By")));
                        }

                        recyclerViewadapter = new ProjectDocumentsAdapter(allProjectDocuments, getApplicationContext());
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
        getAllProjectDocumentsOfUser(getIntent().getStringExtra("projectunique"));
    }
}
