package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddReminder extends AppCompatActivity {

    TextView txtMeetingPartyName;
    AppCompatEditText edtDescriptionForMeet,edtSelectDateForMeet,edtSelectTimeForMeet;
    AppCompatButton btnAddReminderForMeet;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        txtMeetingPartyName = findViewById(R.id.txtMeetingPartyName);
        edtDescriptionForMeet = findViewById(R.id.edtDescriptionForMeet);
        edtSelectDateForMeet = findViewById(R.id.edtSelectDateForMeet);
        edtSelectTimeForMeet = findViewById(R.id.edtSelectTimeForMeet);
        btnAddReminderForMeet = findViewById(R.id.btnAddReminderForMeet);

        txtMeetingPartyName.setText(getIntent().getStringExtra("meetpartyname"));

        final DatePickerDialog.OnDateSetListener meetDate = new DatePickerDialog.OnDateSetListener() {
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

        edtSelectDateForMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddReminder.this, meetDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        edtSelectTimeForMeet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddReminder.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtSelectTimeForMeet.setText( "" + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnAddReminderForMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(edtDescriptionForMeet.getText().toString().isEmpty())
                {
                    edtDescriptionForMeet.setError("Please Enter Description.");
                }
                else if(edtSelectDateForMeet.getText().toString().isEmpty())
                {
                    edtSelectDateForMeet.setError("Please Select Date.");
                }
                else if(edtSelectTimeForMeet.getText().toString().isEmpty())
                {
                    edtSelectTimeForMeet.setError("Please Select Time.");
                }
                else
                {
                    String dt = edtSelectDateForMeet.getText().toString();
                    String[] separatedDT = dt.split("-");

                    String tm = edtSelectTimeForMeet.getText().toString();
                    String[] separatedTM = tm.split(":");

                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(separatedDT[0]),Integer.parseInt(separatedDT[1]) - 1,Integer.parseInt(separatedDT[2]),Integer.parseInt(separatedTM[0]),Integer.parseInt(separatedTM[1]));
                    long endMillis = cal.getTimeInMillis();
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setType("vnd.android.cursor.item/event");
                    intent.putExtra("beginTime", cal.getTimeInMillis());
                    intent.putExtra("allDay", false);
                    intent.putExtra("rrule", "FREQ=DAILY");
                    intent.putExtra("endTime", endMillis);
                    intent.putExtra("title", getIntent().getStringExtra("meetpartyname") + " : " + edtDescriptionForMeet.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void updateLabelFromDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edtSelectDateForMeet.setText(sdf.format(myCalendar.getTime()));
    }
}
