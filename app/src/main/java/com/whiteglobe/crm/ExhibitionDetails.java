package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ExhibitionDetails extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static String TAG = ExhibitionDetails.class.getSimpleName();

    AppCompatImageView imgExhibitionDetailsImage1,imgExhibitionDetailsImage2;
    TextView txtExhibitionPartyName,txtExhibitionPartyPhone,txtExhibitionPartyRemarks;
    AppCompatButton btnSendWhatsappMsgToParty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_details);
        getSupportActionBar().hide();

        imgExhibitionDetailsImage1 = findViewById(R.id.imgExhibitionDetailsImage1);
        imgExhibitionDetailsImage2 = findViewById(R.id.imgExhibitionDetailsImage2);
        txtExhibitionPartyName = findViewById(R.id.txtExhibitionPartyName);
        txtExhibitionPartyPhone = findViewById(R.id.txtExhibitionPartyPhone);
        txtExhibitionPartyRemarks = findViewById(R.id.txtExhibitionPartyRemarks);
        btnSendWhatsappMsgToParty = findViewById(R.id.btnSendWhatsappMsgToParty);

        getExhibitionDetails(getIntent().getStringExtra("exhb_id"));

        final String phno = "+91"+txtExhibitionPartyPhone.getText().toString();

        btnSendWhatsappMsgToParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+91" + phno + "&text=" + "Hello");

                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(sendIntent);
            }
        });
    }

    private void getExhibitionDetails(String exhbid) {

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        showpDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(ExhibitionDetails.this);

        String url = WebName.weburl+"exhibitiondetails.php?exhbid="+exhbid;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url , null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    Picasso.get().load(WebName.imgurl + "exhibitionSIBPL/" + response.getString("Exhb_Image1")).into(imgExhibitionDetailsImage1);
                    Picasso.get().load(WebName.imgurl + "exhibitionSIBPL/" + response.getString("Exhb_Image2")).into(imgExhibitionDetailsImage2);
                    txtExhibitionPartyName.setText(response.getString("Exhb_PartyName"));
                    txtExhibitionPartyPhone.setText(response.getString("Exhb_PhoneNo"));
                    txtExhibitionPartyRemarks.setText(response.getString("Exhb_Remarks"));
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
