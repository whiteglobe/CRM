package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ApplyForLeave extends AppCompatActivity {

    AppCompatButton btnApplyLeave;
    AppCompatEditText leaveFromDate,leaveToDate,leaveReason;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_leave);
        getSupportActionBar().hide();

        btnApplyLeave = findViewById(R.id.btnApplyLeave);
        leaveFromDate = findViewById(R.id.leaveFromDate);
        leaveToDate = findViewById(R.id.leaveToDate);
        leaveReason = findViewById(R.id.leaveReason);

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
                    Toast.makeText(getApplicationContext(),"Best Of Luck.",Toast.LENGTH_LONG).show();
                }
            }
        });
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
