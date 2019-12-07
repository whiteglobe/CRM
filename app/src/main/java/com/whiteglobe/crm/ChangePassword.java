package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    AppCompatEditText chngPwdCurrentPassword,chngPwdNewPassword,chngPwdVerifyNewPassword;
    AppCompatButton btnChangePassword;

    SharedPreferences sessionChangePassword;
    private ProgressDialog pDialog;
    private static String TAG = ChangePassword.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().hide();

        chngPwdCurrentPassword = findViewById(R.id.chngPwdCurrentPassword);
        chngPwdNewPassword = findViewById(R.id.chngPwdNewPassword);
        chngPwdVerifyNewPassword = findViewById(R.id.chngPwdVerifyNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        sessionChangePassword = getSharedPreferences("user_details",MODE_PRIVATE);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(chngPwdCurrentPassword.getText().toString().trim().equals(""))
                {
                    chngPwdCurrentPassword.setError("Please Enter Current Password.");
                }
                else if(chngPwdNewPassword.getText().toString().trim().equals(""))
                {
                    chngPwdNewPassword.setError("Please Enter New Password.");
                }
                else if(chngPwdVerifyNewPassword.getText().toString().trim().equals(""))
                {
                    chngPwdVerifyNewPassword.setError("Please Enter Verify New Password.");
                }
                else if(!chngPwdNewPassword.getText().toString().trim().equals(chngPwdVerifyNewPassword.getText().toString().trim()))
                {
                    showCustomDialogError("New Password and Verify New Password Must Be Same.");
                }
                else if(chngPwdCurrentPassword.getText().toString().trim().equals(chngPwdNewPassword.getText().toString().trim()))
                {
                    showCustomDialogError("Old Password and New Password must not be same.");
                }
                else if(chngPwdNewPassword.getText().toString().trim().equals(chngPwdVerifyNewPassword.getText().toString().trim()))
                {
                    changeUserPassword(sessionChangePassword.getString("uname",null),chngPwdCurrentPassword.getText().toString(),chngPwdNewPassword.getText().toString());
                }
            }
        });
    }

    private void changeUserPassword(final String username,final String currentpassword,final String newpassword){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Changing Password...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"changeuserpassword.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else if(jsonObject.getInt("success") == 0)
                            {
                                showCustomDialogError(jsonObject.getString("msg"));
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
                        Toast.makeText(ChangePassword.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("currentpassword",currentpassword);
                params.put("newpassword",newpassword);

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
}
