package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ApplyForLeave extends AppCompatActivity {

    AppCompatButton btnApplyLeave,btnViewAllLeaves;
    AppCompatEditText leaveFromDate,leaveToDate,leaveReason;
    final Calendar myCalendar = Calendar.getInstance();

    SharedPreferences sessionApplyForLeave;
    private ProgressDialog pDialog;
    private static String TAG = ApplyForLeave.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_leave);
        getSupportActionBar().hide();

        btnApplyLeave = findViewById(R.id.btnApplyLeave);
        btnViewAllLeaves = findViewById(R.id.btnViewAllLeaves);
        leaveFromDate = findViewById(R.id.leaveFromDate);
        leaveToDate = findViewById(R.id.leaveToDate);
        leaveReason = findViewById(R.id.leaveReason);

        sessionApplyForLeave = getSharedPreferences("user_details",MODE_PRIVATE);

        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFromDate();
            }
        };

        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelToDate();
            }
        };

        leaveFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ApplyForLeave.this, dateFrom, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        leaveToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ApplyForLeave.this, dateTo, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(leaveFromDate.getText().toString().trim().equals(""))
                {
                    leaveFromDate.setError("Please Select Appropriate Date.");
                }
                else if(leaveToDate.getText().toString().trim().equals(""))
                {
                    leaveToDate.setError("Please Select Appropriate Date.");
                }
                else if(leaveReason.getText().toString().trim().equals(""))
                {
                    leaveReason.setError("You Have To Enter Reason For Your Leave.");
                }
                else if(leaveFromDate.getText().toString().compareTo(leaveToDate.getText().toString()) > 0)
                {
                    showCustomDialogError("Please Select Appropriate From & To Dates.");
                }
                else if(leaveFromDate.getText().toString().compareTo(leaveToDate.getText().toString()) < 0 || leaveFromDate.getText().toString().compareTo(leaveToDate.getText().toString()) == 0)
                {
                    applyForLeave(sessionApplyForLeave.getString("uname",null),leaveFromDate.getText().toString(),leaveToDate.getText().toString(),leaveReason.getText().toString());
                }
            }
        });

        btnViewAllLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUserLeavesAll = new Intent(ApplyForLeave.this,ViewUserLeaves.class);
                startActivity(iUserLeavesAll);
            }
        });
    }

    private void applyForLeave(final String username,final String fromdate,final String todate,final String reason){

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Applying For Leave...");
        pDialog.show();

        showpDialog();

        String url = WebName.weburl+"applyforleave.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("Response",response);
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getInt("success") == 1)
                            {
                                leaveFromDate.setText("");
                                leaveToDate.setText("");
                                leaveReason.setText("");
                                Toast.makeText(getApplicationContext(),jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                Intent iUserLeaves = new Intent(ApplyForLeave.this,ViewUserLeaves.class);
                                startActivity(iUserLeaves);
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
                        Toast.makeText(ApplyForLeave.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("fromdate",fromdate);
                params.put("todate",todate);
                params.put("reason",reason);

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

    private void updateLabelFromDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        leaveFromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelToDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        leaveToDate.setText(sdf.format(myCalendar.getTime()));
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
