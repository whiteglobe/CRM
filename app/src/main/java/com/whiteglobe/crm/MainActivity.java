package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputEditText txtUsername,txtPassword;
    private ProgressDialog pDialog;
    private static final String KEY_SUCCESS = "success";
    private String usname,pwd,uname,msg;
    private int success;
    SharedPreferences sessionData;
    Intent iLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        getSupportActionBar().hide();

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        sessionData = getSharedPreferences("user_details",MODE_PRIVATE);
        iLogin = new Intent(MainActivity.this,Dashboard.class);
        if(sessionData.contains("username")){
            startActivity(iLogin);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUsername.getText().toString().isEmpty())
                {
                    txtUsername.setError("Please Enter Username");
                }
                else if(txtPassword.getText().toString().isEmpty())
                {
                    txtPassword.setError("Please Enter Password");
                }
                else if(txtUsername.getText().toString().isEmpty() && txtPassword.getText().toString().isEmpty())
                {
                    txtUsername.setError("Please Enter Username");
                    txtPassword.setError("Please Enter Password");
                }
                else if(!txtUsername.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty())
                {
                    usname = txtUsername.getText().toString();
                    pwd = txtPassword.getText().toString();
                    new UserLogin().execute();
                }
            }
        });

        if (!isConnected()) {
            showCustomDialog();
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        (dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

    private class UserLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Logging In. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();

            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put("username", usname);
            httpParams.put("password", pwd);
            String url = WebName.weburl + "login.php";
            //Log.d("URL",url);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    url, "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
                if(success == 1){
                    uname = jsonObject.getString("uname");
                }
                else if (success == 0){
                    msg = jsonObject.getString("msg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        //Toast.makeText(MainActivity.this,"Username: "+uname, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(),txtUsername.getText()+" "+txtPassword.getText(),Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sessionData.edit();
                        editor.putString("username",uname);
                        editor.putString("uname",txtUsername.getText().toString());
                        editor.commit();
                        iLogin = new Intent(MainActivity.this, Dashboard.class);
                        startActivity(iLogin);
                        finish();

                    } else if(success == 0){
                        showCustomDialogError(msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
